/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.st10546887rhmokhasimbeprog5121part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class ST10546887RHMOKHASIMBEPROG5121Part1Test {
    Login login = new Login();
    public ST10546887RHMOKHASIMBEPROG5121Part1Test() {
        
    }

    @org.junit.jupiter.api.Test
    public void testSomeMethod() {
    }
    
    @Test
    public  void CorrectUserName(){
           login.savedUserName= "kyl_1";
           assertTrue(login.checkUserName(login.savedUserName));
        
    }
     @Test
    public  void IncCorrectUserName(){
               login.savedUserName= "kyle!!!!!!!!!";
           assertFalse(login.checkUserName(login.savedUserName));    
        
    }
     @Test
    public  void CorrectPassword(){
            login.savedPassWord ="Ch&&sec@ke99!";
             assertTrue(login.checkPasswordComplexity(login.savedPassWord));
        
    }
     @Test
    public  void InCorrectPassword(){
            login.savedPassWord ="passsword";
          assertFalse(login.checkPasswordComplexity(login.savedPassWord));  
    }
    
     @Test
    public  void CorrectPhone(){
             login.savedCellPhone ="+27838968976";
             assertTrue(login.checkCellPhoneNumber(login.savedCellPhone));
        
    }
    
    
     @Test
    public  void inCorrectPhone(){
            
            login.savedCellPhone ="08966553";
          assertFalse(login.checkCellPhoneNumber(login.savedCellPhone));
          
        
    }
    
    
    
}
