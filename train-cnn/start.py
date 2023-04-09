 
import sys
from model import Model
from metric import Metric 
from time import sleep


id, phenotype, dataset, grammar = Metric.get_metrics_by_status(Metric, "waiting")

print(' \n[STATUS] Searching new phenotype in the training queue \n')

while True:
    if id != None:
        print('[STATUS] '+dataset+' : Phenotype '+phenotype+' not yet trained. Building... \n')
        Metric.update_metric(Metric, id, 0.0, 0.0, 0.0, 0.0, "training")
        model = Model.build_model(Model, phenotype, dataset, grammar)
        
        if model:
            accuracy, accuracy_sd, f1_score, f1_score_sd = Model.train_model(Model, model)
            status = "trained"
        else:
            accuracy, accuracy_sd, f1_score, f1_score_sd = 0.0, 0.0, 0.0, 0.0
            status = "waiting"

        Metric.update_metric(Metric, id, accuracy, accuracy_sd, f1_score, f1_score_sd, status)
        print(grammar, dataset, phenotype, accuracy, accuracy_sd, f1_score, f1_score_sd)   
    else:
        print('[STATUS] No phenotype in the training queue \n')
        
    sleep(15)  
    print('\n[STATUS] Searching new phenotype in the training queue \n')
    id, phenotype, dataset, grammar = Metric.get_metrics_by_status(Metric, "waiting")