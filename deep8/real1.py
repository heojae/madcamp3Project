from PIL import Image
import os
import os
import numpy as np
import cv2
from PIL import Image
from os.path import join
import PIL.Image as pilimg
import numpy as np
import matplotlib.pyplot as plt
import sys

import numpy as np
from keras.models import Model
from keras.preprocessing import image
from keras.applications import imagenet_utils, mobilenet
#import tensorflowjs as tfjs
from keras.models import load_model
model = load_model('n_test2.h5')

test_img_path="\\Users\\q\\Desktop\\deep8\\photos\\IMG_20190113_125911.jpg"
test_img_path="\\Users\\q\\Desktop\\deep8\\photos\\"+sys.argv[1]
img_list = []
#print("aaaa"+sys.argv[1])
print("ready...\n")

img = image.load_img(test_img_path, target_size=(150, 150))
x = image.img_to_array(img)
x = np.expand_dims(x, axis=0)
preds = model.predict(x)

k=preds[0]
if k[0]==1:
    print("angry\n")
elif k[1]==1:
    print("happy\n")
elif k[2]==1:
    print("sad\n")

print(preds)

print("result is ok?")













