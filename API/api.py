from flask import Flask
from usable_model import prediction
from flask import request

'''
appdopatame : nombre de la api
/appdoptame/dog-race : ruta de la api para reconocimiento de raza de un perro
'''




if __name__ == '__main__':
    
    app = Flask(__name__)

    @app.route("/appdoptame/dog-race") #ruta de la api para deteccion de raza de un perro
    #una vez se acceda a la ruta se va a ejecutar esto
    def model_prediction():
        try:
            print("Prediciendo la raza de la foto...")
            img_path = request.args.get('url', default = 1, type = str)
            predict = prediction(img_path)
            print(f"La raza es {predict}")
        except Exception as error:
            predict = f"STATUS CODE: 400 \nError en la prediccion"
        return predict
    
    app.run(debug = True) 