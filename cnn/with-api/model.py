from tensorflow.keras import datasets, layers, models, callbacks, optimizers
from tensorflow.keras import backend as K 
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelBinarizer
#from keras.utils import to_categorical
import numpy as np
import re, csv, os, requests
import tensorflow as tf
import sys

print("[VERSAO] Versão do Python:",sys.version)
print("[VERSAO] Versão do TensorFlow:", tf.__version__)
print("[VERSAO] Versão do Keras:", tf.keras.__version__)

#verifica o nome da GPU disponivel
tf.test.gpu_device_name()
tf.config.list_physical_devices('GPU')

class Model:
    maximise = True
    multi_objective = True
    dataset_name = False
    grammar_name = False
    
    

    def load_data(self):
        
        ########################################
        ### CIFAR 10
        ########################################
        if(self.dataset_name == "cifar10"):            
            shape = (32, 32, 3)
            dataset = np.load('cifar10.npz', allow_pickle=True)            
            
            train = dataset['train'].tolist()
            test = dataset['test'].tolist()

            train_images, test_images, train_labels, test_labels = train['image'], test['image'], train['label'], test['label']

            train_images = train_images.reshape((train_images.shape[0], *shape))
            train_images = train_images.astype("float") / 255.0

            test_images = test_images.reshape((test_images.shape[0], *shape))
            test_images = test_images.astype("float") / 255.0

            validation_images, test_images, validation_labels, test_labels = train_test_split(test_images, test_labels, test_size=0.2, random_state=42)
                     
        ########################################
        ### MNIST
        ########################################

        if(self.dataset_name == "mnist"):
            shape = (28, 28, 1)
            
            dataset = np.load('mnist.npz', allow_pickle=True)            
            
            train = dataset['train'].tolist()
            test = dataset['test'].tolist()

            train_images, test_images, train_labels, test_labels = train['image'], test['image'], train['label'], test['label']

            train_images = train_images.reshape((train_images.shape[0], *shape))
            train_images = train_images.astype("float") / 255.0

            test_images = test_images.reshape((test_images.shape[0], *shape))
            test_images = test_images.astype("float") / 255.0

            validation_images, test_images, validation_labels, test_labels = train_test_split(test_images, test_labels, test_size=0.2, random_state=42)
      
        ########################################
        ### EUROSAT
        ########################################
        
        if(self.dataset_name == 'eurosat'): 
            shape  = (64, 64, 3)
            
            dataset = np.load('eurosat.npz', allow_pickle=True)
            
            train = dataset['train'].tolist()

            train_images, train_labels = train['image'], train['label']

            train_images = train_images.reshape((train_images.shape[0], *shape))
            train_images = train_images.astype("float") / 255.0

            train_images, test_images, train_labels, test_labels = train_test_split(train_images, train_labels, test_size=0.2, random_state=42)
            validation_images, test_images, validation_labels, test_labels = train_test_split(test_images, test_labels, test_size=0.2, random_state=42)
    
        lb = LabelBinarizer()
        train_labels = lb.fit_transform(train_labels)
        validation_labels = lb.transform(validation_labels)
        test_labels = lb.transform(test_labels)

        dataset.close()

        return train_images, train_labels, test_images, test_labels, validation_images, validation_labels


    def build_model(self, phenotype, dataset, grammar):
        self.dataset_name = dataset
        self.gramar_name = grammar        
        classes = 10
        
        if(self.dataset_name == "cifar10"):            
            shape = (32, 32, 3)           
        if(self.dataset_name == "mnist"):
            shape = (28, 28, 1)
        if(self.dataset_name == 'eurosat'): 
            shape  = (64, 64, 3)
            

        # To free memory on google colab.
        if K.backend() == 'tensorflow':
            K.clear_session()

        nconv, npool, nfc, nfcneuron = [int(i) for i in re.findall('\d+', phenotype.split('lr-')[0])]
        has_dropout = 'dropout' in phenotype
        has_batch_normalization = 'bnorm' in phenotype
        has_pool = 'pool' in phenotype
        learning_rate = float(phenotype.split('lr-')[1])

        # number of filters
        filter_size = 32

        model = models.Sequential()
        # model.add(layers.InputLayer(input_shape=self.shape))

        try:        
            # Pooling
            for i in range(npool):        
                # Convolutions
                for j in range(nconv):        
                    model.add(layers.Conv2D(filter_size, (3, 3), activation='relu', padding='same', input_shape=shape))

                    # Duplicate number of filters for each two convolutions
                    if (((i + j) % 2) == 1): filter_size = filter_size * 2

                    # Add batch normalization
                    if has_batch_normalization:
                        model.add(layers.BatchNormalization())

                # Add pooling
                if has_pool:
                    model.add(layers.MaxPooling2D(pool_size=(2, 2)))
                    # Add dropout
                    if has_dropout:
                        model.add(layers.Dropout(0.25))

            model.add(layers.Flatten())

            # fully connected
            for i in range(nfc):
                model.add(layers.Dense(nfcneuron))
                model.add(layers.Activation('relu'))

            if has_dropout:
                model.add(layers.Dropout(0.5))

            model.add(layers.Dense(classes, activation='softmax'))
            # model.summary()

        except Exception as ex:
            # Some NN topologies are invalid
            print(ex)
            return None

        opt = optimizers.Adam(lr=learning_rate)

        # F1 Score metric function
        def f1_score(y_true, y_pred):
            true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
            possible_positives = K.sum(K.round(K.clip(y_true, 0, 1)))
            predicted_positives = K.sum(K.round(K.clip(y_pred, 0, 1)))
            precision = true_positives / (predicted_positives + K.epsilon())
            recall = true_positives / (possible_positives + K.epsilon())
            f1_val = 2 * (precision * recall) / (precision + recall + K.epsilon())
            return f1_val

        model.compile(loss='categorical_crossentropy', optimizer=opt, metrics=['accuracy', f1_score])
        
        return model


    def train_model(self, model):

        accuracies, f1_scores = [], []

        train_images, train_labels, test_images, \
            test_labels, validation_images, validation_labels = self.load_data(self)

        # Train three times
        for i in range(3):

            print('Trainning %s of 3' % (i + 1))

            # Early Stop when bad networks are identified        
            es = callbacks.EarlyStopping(monitor='val_accuracy', mode='max', verbose=1, patience=10, baseline=0.5)

            model.fit(train_images, train_labels, epochs=70, batch_size=128, 
                validation_data=(validation_images, validation_labels), callbacks=[es])
            
            loss, accuracy, f1_score = model.evaluate(test_images, test_labels, verbose=1)

            accuracies.append(accuracy)
            f1_scores.append(f1_score)

            if i == 0 and accuracy < 0.5:
                break

        return np.mean(accuracies), np.std(accuracies), np.mean(f1_scores), np.std(f1_scores)
