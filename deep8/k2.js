var express = require('express');
var app = express();
var done = false;

var mime = require('mime');
var fs = require('fs');
var new_token;

var url = require('url');
var querystring = require('querystring'); 
var multer = require('multer')





app.listen(80, function() {
    console.log('server running on port 80');
} )

// Function callName() is executed whenever
// the URL is of the form localhost:3000/name
app.get('/name', callName)

function callName(req, res) {
    console.log("/name come");

    var spawn = require("child_process").spawn;


    // E.g.: http://localhost:3000/name?firstname=Mike&lastname=Will
    // So, first name = Mike and last name = Will
    firstname=req.query.firstname;
    var process = spawn('python',["./re_test1.py",
                            req.query.firstname,
                            req.query.lastname] );

    // Takes stdout data from script which executed
    // with arguments and send this data to res object
    process.stdout.on('data', function(data) {
        res.send(data.toString());
    } )
}






let users = [
    {
      id: 1,
      name: 'alice'
    },
    {
      id: 2,
      name: 'bek'
    },
    {
      id: 3,
      name: 'chris'
    }
  ]
  
  app.get('/users', (req, res) => {
     console.log('who get in here/users');
     res.json(users)
  });
  app.get('/:photoid', (req, res) => {


    var photoid = req.params.photoid

    console.log('who get in here/photoid');
    console.log("firstname:  "+firstname);
    var filename = '\\Users\\q\\Desktop\\deep8\\photos\\'+photoid;
    console.log("filename   "+filename)
    fs.readFile(filename,              //파일 읽기
        function (err, data)
        {
            //http의 헤더정보를 클라이언트쪽으로 출력
            //image/jpg : jpg 이미지 파일을 전송한다
            //write 로 보낼 내용을 입력
            res.writeHead(200, { "Context-Type": "image/jpg" });//보낼 헤더를 만듬
            res.write(data);   //본문을 만들고
            res.end();  //클라이언트에게 응답을 전송한다

        }
    );

  });
  
  app.post('/post', (req, res) => {
     console.log('who get in here post /users');
     var inputData;
  
     req.on('data', (data) => {
       inputData = JSON.parse(data);
     });
  
     req.on('end', () => {
       console.log("user_id : "+inputData.user_id + " , name : "+inputData.name);
     });
  
     res.write("OK!");
     res.end();
  });



app.use(multer({
    dest: './photos/',
    rename: function (fieldname, filename) {
        return filename;
    },
    onFileUploadStart: function (file) {
        console.log(file.originalname + ' is starting ...')
    },
    onFileUploadComplete: function (file) {
        console.log(file.fieldname + ' uploaded to  ' + file.path)
        done = true;
    }
}));

app.get('/upload', function(req, res){
    // 사진 업로드 창
    console.log("aaaaaaa");
    res.sendfile('index.html');
});

app.get('/upload/2', function(req, res){
    // 사진 업로드 창
    res.sendfile('index.html');
});

app.post('/api/photo', function (req, res) {
    // 사진 업로드 post
    if (done == true) {
        console.log(req.files);
        console.log(req.files.uploaded_file.name);
        res.end("File uploaded.\n" + JSON.stringify(req.files));
    }
});
