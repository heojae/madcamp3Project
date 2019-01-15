package com.example.q.deep5;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.squareup.picasso.Picasso;



public class MainActivity extends AppCompatActivity {

    Button btn_take = null;
    ImageView iv = null;
//    TextView tvData;
    static String uploadFilePath = "/storage/emulated/0/DCIM/Camera/";
    static String uploadFileName = "IMG_20190113_125911.jpg";
    TextView messageText;
    Button uploadButton, button1;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String upLoadServerUri = null;
    //ImageView imView;
    static int checking=0;
    static int d_check=0;
    String imgUrl = "http://143.248.39.118/three_friends.PNG";

    Bitmap bmImg;


    static int making=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestAllPermission();

        button1=(Button)findViewById(R.id.button1);
        //imView = (ImageView) findViewById(R.id.imageView1);
        //uploadButton = (Button) findViewById(R.id.uploadButton);

        /************* Php script path ****************/
        upLoadServerUri = "http://143.248.39.118:80/api/photo";
/*
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                dialog = ProgressDialog.show(MainActivity.this, "", "Uploading file...", true);

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //messageText.setText("uploading started.....");
                            }
                        });

                        uploadFile(uploadFilePath +uploadFileName);
                        //uploadFile(uploadFilePath+uploadFileName);
                        new JSONTask2().execute("http://143.248.39.118:80/name?"+"firstname="+uploadFileName);
                    }
                }).start();

            }
        });
*/

        btn_take = (Button)findViewById(R.id.btn_take);
        iv = (ImageView)findViewById(R.id.iv);

        btn_take.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                checking=0;
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (checking==1) {
                        Picasso.with(MainActivity.this)
                                .load("http://143.248.39.118/" + "re_" + uploadFileName)
                                .into(iv);
                        //tvData.setText("is it right?");
                        Toast.makeText(MainActivity.this, "이미지를 확인해주세요", Toast.LENGTH_LONG).show();
                    }
                    else if(checking==0 && d_check==1){
                        Toast.makeText(MainActivity.this, "사진에 얼굴이 없습니다", Toast.LENGTH_LONG).show();
                    }
                    else if(checking==0 && d_check==0){
                        Toast.makeText(MainActivity.this, "아직 적용중입니다.", Toast.LENGTH_LONG).show();
                    }

            }

        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)//사진들고오기 결과물
    {
        iv.setImageURI(data.getData());
        getPath(data.getData());

        Uri uri = data.getData();
        iv.setImageURI(data.getData());
        Log.i("imggg",""+uri);
        Toast.makeText(getApplicationContext(),""+uri,Toast.LENGTH_SHORT);

        dialog = ProgressDialog.show(MainActivity.this, "", "Uploading file...", true);

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("uploading started.....");
                    }
                });

                uploadFile(uploadFilePath +uploadFileName);
                //uploadFile(uploadFilePath+uploadFileName);
                new JSONTask2().execute("http://143.248.39.118:80/name?"+"firstname="+uploadFileName);
            }
        }).start();


    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        Log.i("imgggaaaaaaa",""+uri);
        Log.i("imgggbbbbbbb",cursor.getString(columnIndex));
        String pull_path=cursor.getString(columnIndex);
        String[] words = pull_path.split("/");
        String b="";
        for(int j=0; j<words.length-1; j++){
            b=b+words[j]+"/";
            Log.i("aaaaaaaaaaaaa",b);
        }
        Log.i("aaaaaa",words[words.length-1]);
        uploadFileName=words[words.length-1];
        uploadFilePath=b;
        Toast.makeText(getApplicationContext(),""+uri,Toast.LENGTH_SHORT);
        return cursor.getString(columnIndex);
    }

    // perpose of post 인데 현재 목적은 Ok 받아오기 뿐
    public class JSONTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "androidTest");
                jsonObject.accumulate("name", "yun");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //tvData.setText(result);//서버로 부터 받은 값을 출력해주는 부
            if (result.equals("result is uploaded and check it")) {
                Toast.makeText(MainActivity.this, "이미지가 적용 되었습니다. 확인해주세요", Toast.LENGTH_LONG).show();

            }
        }
    }













    //perpose of get으로 사진 보낸것 기반의 결과물을 들고 오기
    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "androidTest");
                jsonObject.accumulate("name", "yun");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //tvData.setText(result);

            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            if (result.contains("no face")){
                Toast.makeText(MainActivity.this, "사진에 얼굴이 없습니다.", Toast.LENGTH_LONG).show();
                checking=0;
                d_check=1;
            }
            else{
                Toast.makeText(MainActivity.this, "이미지가 적용 되었습니다. 확인해주세요", Toast.LENGTH_LONG).show();
                checking=1;
                d_check=0;

            }


        }

    }


    //사진 업로드하는 기능
    public int uploadFile(String sourceFileUri) {
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + "/storage/emulated/0/DCIM/Camera/IMG_20190106_084159.jpg");

            runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("Source File not exist :"
                    //        + "/storage/emulated/0/DCIM/Camera/IMG_20190106_084159.jpg");
                }
            });

            return 0;

        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);
                //dos.writeBytes(jsonObject.toString());
                dos.writeBytes(lineEnd);




                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()),8192);
                    final StringBuilder response = new StringBuilder();
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + response.toString();

                            //messageText.setText(msg);
                            Toast.makeText(MainActivity.this, "적용중입니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();



            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(MainActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(MainActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }







    private void requestAllPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) { }
            else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1); }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) { }
            else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 2); }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) { }
            else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 3); }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1 : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { }
                else { }
                return;
            }
            case 2 : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { }
                else { }
                return;
            }
            case 3 : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { }
                else { }
                return;
            }

        }
    }





}