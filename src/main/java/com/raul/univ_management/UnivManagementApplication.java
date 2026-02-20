/** Clasa principala pentru pornirea aplicatiei Spring Boot
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/

package com.raul.univ_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UnivManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnivManagementApplication.class, args); 
    }

}
