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
import numpy as np
import matplotlib.pyplot as plt
import numpy as np
from keras.models import Model
from keras.preprocessing import image
from keras.applications import imagenet_utils, mobilenet
#import tensorflowjs as tfjs
def softmax(x):
    e_x = np.exp(x - np.max(x))
    return e_x / e_x.sum()



from keras.models import load_model
#model = load_model('n_test2.h5')
#model = load_model('face_5.h5')
model = load_model('CNN3.h5')


#path_dir="\\Users\\q\\Desktop\\deep8\\datasets\\emulate"
path_dir="\\Users\\q\\Desktop\\deep8\\datasets\\emotion\\train\\anger"

test_img_path="\\Users\\q\\Desktop\\mad3_picture\\happy4.PNG"
img_list = []

first=0
second=0
third=0


file_list=os.listdir(path_dir)
for item in file_list :
    if item.find('.PNG') is not -1 :
        #if item.find("new_"):
        img_list.append(item)
        test_path=path_dir+"\\"+item
        print("aaaaa")
        print(test_path)
        img = image.load_img(test_path, target_size=(150, 150))
        x = image.img_to_array(img)

        x = np.expand_dims(x, axis=0)

        y = softmax(x)
        print("y :   ")
        print(np.sum(y))
        # x = preprocess_input(x)


        preds = model.predict(x)

        k=preds[0]
        print(k)
        if k[0]==1:
            first=first+1
        elif k[1]==1:
            second=second+1
        elif k[2]==1:
            third=third+1

        print(preds)


print("first: anger :" )
print(first)
print("second: happy :")
print(second)
print("third: sad :" )
print(third)
'''

from keras.models import load_model
model = load_model('n_test2.h5')





img = image.load_img(test_img_path, target_size=(150, 150))
x = image.img_to_array(img)
x = np.expand_dims(x, axis=0)
plt.show(x)

#x = preprocess_input(x)

preds = model.predict(x)

print(preds)










#pImg = process_image(test_img_path)
#print(pImg)

#yhat = model.predict_classes(pImg)




#mobilenet = mobilenet.MobileNet()

#prediction = mobilenet.predict(pImg)
#results = imagenet_utils.decode_predictions(prediction)
#print(results)






'''




'''

for name in img_list :
    img = Image.open(name)
    img_array = np.array(img)
    img_resize = cv2.resize(img_array, (128, 128), interpolation=cv2.INTER_AREA)
    img = Image.fromarray(img_resize)
 #   img.save("new_sad"+name)


    img3 = img.rotate(-10)
    img3.save("rotation_minus_10_"+name)

    img4 = img.rotate(10)
    img4.save("rotation_10_" + name)

    img2=img.transpose(Image.FLIP_LEFT_RIGHT)
    img2.save("reverse_" + name)


    #img.save("rotaion_"+name)
    print(name + '   ' + str(index) + '/' + str(total_image))
    index = index + 1

'''