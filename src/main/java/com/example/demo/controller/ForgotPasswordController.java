package com.example.demo.controller;


import com.example.demo.model.AppUser;
import com.example.demo.model.ForgotPassword;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.ForgotPasswordRepository;
import com.example.demo.service.EmailService;
import com.example.demo.utils.ChangePassword;
import com.example.demo.utils.EmailBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private final AppUserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(AppUserRepository userRepository, ForgotPasswordRepository forgotPasswordRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    //send email for email verification
    @PostMapping("/verification/{email}")
    public ResponseEntity<String> verfication(@PathVariable String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("please provide valide email"));

        int otp = otpGenerator();
        EmailBody EmailBody = com.example.demo.utils.EmailBody.builder()
                .to(email)
                .text("This is the OTP for your forgot password request: "+ otp)
                .subject("OTP for forgot password request")
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+10 * 60 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(EmailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email send for verification!");
    }

    @PostMapping("/verifyOTP/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("please provide valide email"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(()-> new UsernameNotFoundException("Invalide otp for this email" + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("Otp has expired", HttpStatus.EXPECTATION_FAILED);
        }

        forgotPasswordRepository.deleteById(fp.getFpid());
        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(
            @RequestBody ChangePassword changePassword,
            @PathVariable String email) {

        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }

        // Save the password in plain text (not recommended for production).
        // Original hashed/encoded version is commented for future use.
        String plainPassword = changePassword.password();
        userRepository.updatePassword(email, plainPassword);



        /*
         * Recommended secure approach (commented for reference):
         * String encodedPassword = passwordEncoder.encode(changePassword.password());
         * userRepository.updatePassword(email, encodedPassword);
         */

        return ResponseEntity.ok("Password has been changed");
    }




    private Integer otpGenerator() {
        Random random = new Random();
        return 100_000 + random.nextInt(900_000); // Generate a number between 100000 and 999999
    }

}
