import cv2, sys, re
from PIL import Image
import numpy as np
# 입력 파일 지정하기
from keras.models import load_model
model = load_model('n_test2.h5')


#image_file = "\\Users\\q\\Desktop\\deep8\\photos\\Screenshot_20190114-030058_KakaoTalk.jpg"
#file_name="Screenshot_20190114-030058_KakaoTalk.jpg"
image_file = "\\Users\\q\\Desktop\\deep8\\photos\\"+sys.argv[1]
file_name=sys.argv[1]
path="\\Users\\q\\Desktop\\deep8\\photos"

import os
for file in os.listdir('\\Users\\q\\Desktop\\deep8\\photos'):
    #if file.endswith("trim.png"):
    if "trim.jpg" in file:
        #print(file)
        os.remove('\\Users\\q\\Desktop\\deep8\\photos\\'+file)
# 캐스캐이드 파일 경로 지정하기
cascade_file = "haarcascade_frontalface_alt.xml"
# 이미지 읽어 들이기
nature_image = cv2.imread(image_file)
# 그레이스케일 변환
image_gs = cv2.cvtColor(nature_image, cv2.COLOR_BGR2GRAY)
# 얼굴 인식 실행하기
cascade = cv2.CascadeClassifier(cascade_file)
face_list = cascade.detectMultiScale(image_gs, scaleFactor=1.1, minNeighbors=1, minSize=(50, 50))

if len(face_list) == 0:
    print("no face")
    quit()
# 확인한 부분에 모자이크 걸기
#print(face_list)
k=0
for ( x, y, w, h) in face_list:
    # 얼굴 부분 자르기
    face_img = nature_image[y:y+h, x:x+w]
    k=k+1

    cv2.imwrite(os.path.join(path , str(k)+"org_trim.jpg"), face_img)
    cv2.rectangle(nature_image, (x, y), (x + w, y + h), (0,255,0), thickness=8)
    cv2.putText(nature_image, str(k), (x + 10, y + 10), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 2)

#cv2.imwrite(os.path.join(path , "re_"+file_name), image)



from keras.preprocessing import image

path_dir="\\Users\\q\\Desktop\\deep8\\photos"
img_list = []

file_list=os.listdir(path_dir)
for item in file_list :
    if item.find('trim.jpg') is not -1 :
        #if item.find("new_"):
        img_list.append(item)

total_image = len(img_list)
index = 0
#print(img_list)




face_range_list=[]

z=-1
for name in img_list :
    z=z+1
    A=[]
    img = image.load_img(path_dir+"\\"+name, target_size=(150, 150))
    x = image.img_to_array(img)
    x = np.expand_dims(x, axis=0)
    preds = model.predict(x)

    k=preds[0]
    #print(str(z)+"times")
    if k[0]==1:
        #print("angry")
        A=[face_list[z],0]
        face_range_list.append(A)
    elif k[1]==1:
        #print("happy")
        A = [face_list[z], 1]
        face_range_list.append(A)
    elif k[2]==1:
        #print("sad")
        A = [face_list[z], 2]
        face_range_list.append(A)
    #print(preds)

    #print("result is ok?")

for i in range(0,len(face_range_list)):
    (x, y, w, h) = face_list[i]

    #print(w)
    #print("\n")

    if face_range_list[i][1]==0:
        cv2.putText(nature_image, "angry", (x + w//8, y  - h//8), cv2.FONT_HERSHEY_SIMPLEX, 2, (0, 255, 255), 4)
    if face_range_list[i][1]==1:
        cv2.putText(nature_image, "happy", (x + w//8, y  - h//8), cv2.FONT_HERSHEY_SIMPLEX, 2, (0, 255, 255), 4)
    if face_range_list[i][1] == 2:
        cv2.putText(nature_image, "sad", (x + w//8, y - h//8), cv2.FONT_HERSHEY_SIMPLEX, 2, (0, 255, 255), 4)

cv2.imwrite(os.path.join(path , "re_"+file_name), nature_image)


print("result is uploaded and check it")


