
import sys
import requests

class Metric:   

    url = 'https://mestrado.amersonchagas.com.br/api/v1/metrics/'

    def save_metric(self, grammar, dataset, phenotype, accuracy, accuracy_sd, f1_score, f1_score_sd):
        data = {
            'grammar': grammar,
            'dataset': dataset,
            'phenotype': phenotype,
            'accuracy': accuracy,
            'accuracy_sd': accuracy_sd,
            'f1_score': f1_score,
            'f1_score_sd': f1_score_sd,
            'status': "trained"
        }
        r = requests.post(self.url, json=data)

    def update_metric(self, id, accuracy, accuracy_sd, f1_score, f1_score_sd, status):
        data = {
            'id': id,
            'accuracy': accuracy,
            'accuracy_sd': accuracy_sd,
            'f1_score': f1_score,
            'f1_score_sd': f1_score_sd,
            'status': status
        }
        r = requests.post(self.url+"update", json=data)

    def get_metric(self, grammar, phenotype):
        accuracy, accuracy_sd, f1_score, f1_score_sd = None, None, None, None
        
        r = requests.get(self.url+"search", params={
            'dataset': self.dataset_name,
            'phenotype': phenotype,
            'grammar': grammar
        })

        data = r.json()

        #print(data)

        if len(data):
            data = data[0]
            accuracy = float(data['accuracy'])
            accuracy_sd = float(data['accuracy_sd'])
            f1_score = float(data['f1_score'])
            f1_score_sd = float(data['f1_score_sd'])

        return accuracy, accuracy_sd, f1_score, f1_score_sd

    def get_metrics_by_status(self, status):       

        id, phenotype, dataset  = None, None, None

        r = requests.get(self.url+"status", params={
            'status': status
        })

        data = r.json()

        if len(data):
            data = data[0]
            id = data['id']
            phenotype = data['phenotype']
            dataset = data['dataset']
            grammar = data['grammar']

        return id, phenotype, dataset, grammar
