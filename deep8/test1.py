import keras
#keras.__version__
import os, shutil
import keras_resnet.models


# 원본 데이터셋을 압축 해제한 디렉터리 경로
original_dataset_dir = '.\\datasets\\emotion\\train'

# 소규모 데이터셋을 저장할 디렉터리
base_dir = '.\\datasets\\emotion'

#os.mkdir(base_dir)

# 훈련, 검증, 테스트 분할을 위한 디렉터리

train_dir = os.path.join(base_dir, 'train')
print("train_dir :"+train_dir)
#os.mkdir(train_dir)
validation_dir = os.path.join(base_dir, 'validation')
print("validation_dir :"+validation_dir)
#os.mkdir(validation_dir)
test_dir = os.path.join(base_dir, 'test')
print("test_dir :"+test_dir)
#os.mkdir(test_dir)

# 훈련용 화남 사진 디렉터리
train_anger_dir = os.path.join(train_dir, 'anger')
print("train_anger_dir :"+train_anger_dir)

# 훈련용 해피 사진 디렉터리
train_happy_dir = os.path.join(train_dir, 'happy')
print("train_happy_dir :"+train_happy_dir)
# 훈련용 슬픔 사진 디렉터리
train_sad_dir = os.path.join(train_dir, 'sad')
print("train_sad_dir :"+train_sad_dir)



# 검증용 화남 사진 디렉터리
validation_anger_dir = os.path.join(validation_dir, 'anger')
print("validation_anger_dir :"+validation_anger_dir)
#os.mkdir(validation_anger_dir)

# 검증용 해피 사진 디렉터리
validation_happy_dir = os.path.join(validation_dir, 'happy')
print("validation_happy_dir :"+validation_happy_dir)

#os.mkdir(validation_happy_dir)

# 검증용 슬픔 사진 디렉터리
validation_sad_dir = os.path.join(validation_dir, 'sad')
#os.mkdir(validation_sad_dir)
print("validation_sad_dir :"+validation_sad_dir)





# 테스트용 화남 사진 디렉터리
test_anger_dir = os.path.join(test_dir, 'anger')
print("test_anger_dir :"+test_anger_dir)

#os.mkdir(test_anger_dir)

# 테스트용 해피 사진 디렉터리
test_happy_dir = os.path.join(test_dir, 'happy')
print("test_happy_dir :"+test_happy_dir)
#os.mkdir(test_happy_dir)

# 테스트용 슬픔 사진 디렉터리
test_sad_dir = os.path.join(test_dir, 'sad')
print("test_sad_dir :"+test_sad_dir)
#os.mkdir(test_sad_dir)











print('훈련용 anger 이미지 전체 개수:', len(os.listdir(train_anger_dir)))
print('훈련용 happy 이미지 전체 개수:', len(os.listdir(train_happy_dir)))
print('훈련용 sad 이미지 전체 개수:', len(os.listdir(train_sad_dir)))




print('검증용 anger 이미지 전체 개수:', len(os.listdir(validation_anger_dir)))
print('검증용 happy 이미지 전체 개수:', len(os.listdir(validation_happy_dir)))
print('검증용 sad 이미지 전체 개수:', len(os.listdir(validation_sad_dir)))


print('테스트용 anger 이미지 전체 개수:', len(os.listdir(test_anger_dir)))
print('테스트용 happy 이미지 전체 개수:', len(os.listdir(test_happy_dir)))
print('테스트용 sad 이미지 전체 개수:', len(os.listdir(test_sad_dir)))




from keras import layers
from keras import models
from keras import optimizers
from keras.layers.normalization import BatchNormalization
'''
model = models.Sequential()
model.add(layers.Conv2D(32, (3, 3), activation='relu',
                        input_shape=(150, 150, 3)))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(64, (3, 3), activation='relu'))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.Conv2D(256, (3, 3), activation='relu'))
model.add(BatchNormalization())
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.Dropout(0.5))
model.add(layers.Conv2D(256, (3, 3), activation='relu'))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.Flatten())
model.add(layers.Dropout(0.5))
model.add(layers.Dense(512, activation='relu'))
model.add(layers.Dense(3, activation='softmax'))

model.compile(loss='categorical_crossentropy',
              optimizer=optimizers.RMSprop(lr=1e-4),
              metrics=['acc'])


model = models.Sequential()
model.add(layers.Conv2D(32, (3, 3), activation='relu',
                        input_shape=(150, 150, 3)))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(64, (3, 3), activation='relu'))
model.add(layers.Conv2D(64, (3, 3), activation='relu'))

model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.MaxPooling2D((2, 2)))

model.add(layers.Flatten())
model.add(layers.Dropout(0.5))
model.add(layers.Dense(512, activation='relu'))
model.add(layers.Dense(3, activation='softmax'))

model.compile(loss='categorical_crossentropy',
              optimizer=optimizers.RMSprop(lr=1e-4),
              metrics=['acc'])

'''
shape, classes = (150,150,3), 3
x = keras.layers.Input(shape)
model = keras_resnet.models.ResNet34(x, classes = classes)
model.compile("adam","categorical_crossentropy",["accuracy"])

model.summary()
from keras.preprocessing.image import ImageDataGenerator


train_datagen = ImageDataGenerator(rescale=1./255,)

# 검증 데이터는 증식되어서는 안 됩니다!
test_datagen = ImageDataGenerator(rescale=1./255)

train_generator = train_datagen.flow_from_directory(
        # 타깃 디렉터리
        train_dir,
        # 모든 이미지를 150 × 150 크기로 바꿉니다
        target_size=(150, 150),
        batch_size=32,
        # binary_crossentropy 손실을 사용하기 때문에 이진 레이블을 만들어야 합니다
        class_mode='categorical')

validation_generator = test_datagen.flow_from_directory(
        validation_dir,
        target_size=(150, 150),
        batch_size=32,
        class_mode='categorical')


history = model.fit_generator(
      train_generator,
      steps_per_epoch=100,
      epochs=60,
      validation_data=validation_generator,
      validation_steps=10)
model.save('CNN3.h5')


import matplotlib.pyplot as plt

acc = history.history['acc']
val_acc = history.history['val_acc']
loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(1,len(acc)+1)

plt.plot(epochs,acc,'bo',label='Training acc')
plt.plot(epochs, val_acc,'b',label='Validation acc')
plt.title('Training and valiation accuracy')
plt.legend()

plt.figure()

plt.plot(epochs,loss,'bo',label='Training loss')
plt.plot(epochs, val_acc,'b',label='Validation loss')
plt.legend()

plt.show()



'''
from keras.models import load_model

#model = load_model('cats_and_dogs_small_2.h5')
#model = load_model('n_test2.h5')

test_generator = test_datagen.flow_from_directory(
    test_dir,
    target_size=(150,150),
    batch_size=20,
    class_mode='categorical'
)

print(test_generator)


test_loss, test_acc = model.evaluate_generator(test_generator, steps=50)
#test_loss, test_acc = model.evaluate_generator("\\Users\\q\\Desktop\\deep8\\datasets\\emotion\\train\\happy\\new_happy1.png", steps=50)
print('testacc ',test_acc)

model.save('CNN1.h5')



#plt.plot(epochs, loss, 'bo', label='Training loss')
#plt.plot(epochs, val_loss, 'b', label='Validation loss')
#plt.title('Training and validation loss')
#plt.legend()

#plt.show()

'''

