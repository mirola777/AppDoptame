import os
import numpy as np
import pandas as pd
import cv2
import tensorflow as tf

def read_image(path, size):
    image = cv2.imread(path, cv2.IMREAD_COLOR)
    image = cv2.resize(image, (size, size))
    image = image / 255.0
    image = image.astype(np.float32)
    return image

if __name__ == "__main__":
    labels_df = pd.read_csv("labels.csv")
    breed = labels_df["breed"].unique()

    breed2id = {name: i for i, name in enumerate(breed)}
    id2breed = {i: name for i, name in enumerate(breed)}

    ## Model
    model = tf.keras.models.load_model("model.h5")

    image = read_image("springerSpaniel.jpg", 224)
    image = np.expand_dims(image, axis=0)
    pred = model.predict(image)
    label_idx = np.argmax(pred)
    breed_name = id2breed[label_idx]

    print(breed_name)