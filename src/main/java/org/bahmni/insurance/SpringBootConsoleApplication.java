package org.bahmni.insurance;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.bahmni.insurance"})
public class SpringBootConsoleApplication {
//   @Autowired
//   DataSource dataSource;

   public static void main(String[] args) throws Exception {
      SpringApplication.run(SpringBootConsoleApplication.class, args);
      System.out.println("********** Bahmni-Insurance Service started successfully ************** ");
   }
}