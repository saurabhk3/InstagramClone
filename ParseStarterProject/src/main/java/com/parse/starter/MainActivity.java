/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{
  EditText username,password;
  TextView switchText;
  Button button;
  Boolean signUpMode = true;
  ImageView logo;
  RelativeLayout layout;

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
    if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()== KeyEvent.ACTION_DOWN){
      signup(view);
    }
    return false;
  }

  @Override
  public void onClick(View view) {
    if(view.getId()== R.id.switchText){
      if(signUpMode){
        signUpMode = false;
        button.setText("Login");
        switchText.setText("Or, Sign Up");
      }else{
        signUpMode = true;
        button.setText("Sign Up");
        switchText.setText("Or, Login");

      }
    }else if(view.getId()==R.id.logo || view.getId()==R.id.background){
      InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
  }
  public void showUser(){
    Intent intent = new Intent(getApplicationContext(),UserActivity.class);
    startActivity(intent);
  }

  public void signup(View view){
    if(username.getText().toString().equals("") || password.getText().toString().equals("")){
      Toast.makeText(this,"Username/Password Required!",Toast.LENGTH_LONG).show();
    }else{
      if(signUpMode) {
         // first time - sign up
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("Logged Status:", "Successful");
              showUser();
            }
            else
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
          }
        });
      }else{
        // Login
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser parseUser, ParseException e) {
            if(parseUser != null){
              Log.i("Logged ","Okayyy");
              showUser();
            }else{
              Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
          }
        });

      }
    }
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    switchText = (TextView) findViewById(R.id.switchText);
    button = (Button) findViewById(R.id.button);
    username = (EditText) findViewById(R.id.username);
    password = (EditText) findViewById(R.id.password);

    logo = (ImageView) findViewById(R.id.logo);
    layout = (RelativeLayout) findViewById(R.id.background);
    switchText.setOnClickListener(this);
    password.setOnKeyListener(this);
    logo.setOnClickListener(this);
    layout.setOnClickListener(this);

    if(ParseUser.getCurrentUser()!=null){  // is user is logged in already, show the list
      Log.i("USer Current",ParseUser.getCurrentUser().getUsername());
      showUser();
    }
  }



}