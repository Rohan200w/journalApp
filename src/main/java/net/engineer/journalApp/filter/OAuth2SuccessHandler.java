package net.engineer.journalApp.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.engineer.journalApp.entity.User;
import net.engineer.journalApp.services.UserService;
import net.engineer.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");

        // Check if this Google user exists in your MongoDB already
        User existingUser = userService.findByName(email);

        if (existingUser == null) {
            // First time this person logs in via Google — create their account
            User newUser = new User();
            newUser.setUserName(email);
            newUser.setEmail(email);
            newUser.setPassword(UUID.randomUUID().toString()); // dummy — never used
            newUser.setRoles(List.of("USER"));
            userService.saveEntry(newUser); // saves to MongoDB
        }

        // Generate JWT — same as your /public/login does
        String jwt = jwtUtil.generateToken(email);

        response.setContentType("application/json");
        response.getWriter().write(
                "{\"token\": \"" + jwt + "\", \"email\": \"" + email + "\", \"name\": \"" + name + "\"}"
        );
    }
}
