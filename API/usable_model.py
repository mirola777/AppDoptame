import numpy as np
import pandas as pd
import cv2
import tensorflow as tf
import urllib.request as ur



def read_img(img_path: str, size=224):
    response = ur.urlopen(img_path)
    image = np.asarray(bytearray(response.read()), dtype="uint8")
    image = cv2.imdecode(image, cv2.IMREAD_COLOR)
    image = cv2.resize(image, (size, size))
    image = image / 255.0
    image = image.astype(np.float32)
    return image



def prediction(img_path:str) -> str:
    labels = pd.read_csv("C:\\Users\\Admin\\OneDrive - Universidad EAFIT\\Sistemas - EAFIT\\6to Semestre\\P1\\AppDoptame\\dogRacePrediction\\labels.csv")
    breed = labels["breed"].unique()
    
    id2breed = {i: name for i, name in enumerate(breed)}
    
    model =  tf.keras.models.load_model("C:\\Users\\Admin\\OneDrive - Universidad EAFIT\\Sistemas - EAFIT\\6to Semestre\\P1\\AppDoptame\\dogRacePrediction\\model\\model.h5")
    image = read_img(img_path)
    image = np.expand_dims(image, axis= 0)
    prediction = model.predict(image)
    label_id = np.argmax(prediction)
    breed_name = id2breed[label_id]
    return breed_name

