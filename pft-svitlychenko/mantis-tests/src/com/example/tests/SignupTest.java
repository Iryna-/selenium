package com.example.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.example.fw.AccountHelper;
import com.example.fw.JamesHelper;
import com.example.fw.User;

public class SignupTest extends TestBase{

	public User user = new User().setUsername("testuser1").setPassword("123456").setEmail("testuser1@localhost.localdomain");
	private JamesHelper james;
	private AccountHelper accHelper;
	
	
			
	
		@BeforeClass
		public void createMailUser(){
				james = app.getJamesHelper();
				accHelper = app.getAccountHelper();
			if (! james.doesUserExist(user.username)){
				james.createUser(user.username, user.password);
			}
			
		}
	
        @Test
        public void newUserShouldSignUp(){
                
        		accHelper.signUp(user);
        		accHelper.login(user);
                assertThat(accHelper.loggedInUser(), equalTo (user.username));
               
        }
        
        @Test
        public void registeredUserShouldNotSignUp(){
                try {
                	accHelper.signUp(user);
                } catch (Exception e) {
                	
                	assertThat(e.getMessage(), containsString ("That username already being used"));
                	return;
                }
                
                fail("Exception Expected");
        }
        
        @AfterClass
		public void deleteMailUser(){
        	if (james.doesUserExist(user.username)){
        		james.deleteUser(user.username);
		}
        }
}

