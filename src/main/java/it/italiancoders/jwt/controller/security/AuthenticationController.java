package it.italiancoders.jwt.controller.security;
import it.italiancoders.jwt.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
@RestController
public class AuthenticationController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private LogService logSrv;

    @Value("${jwt.route.authentication.maxLoginFailure}")
    private int maxLoginFailure;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRep;

    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device, HttpServletRequest request) throws AuthenticationException {

            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken( authenticationRequest.getUsername(), authenticationRequest.getPassword() ) );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            User user = userRep.findByUsername(authenticationRequest.getUsername());

            if (!user.isEnabled())
                throw new GenericErrorException("Utente disabilitato!");

            if (user.isPasswordExpirable()) {
                userService.doExpirityPasswordValidation(authenticationRequest.getUsername());
            }

            user.setLoginFailed(0);
            user = userRep.save(user);

            List<Authority> list = user.getAuthorities();

            final String token = jwtTokenUtil.generateToken(userDetails, device);
            logSrv.insLog("login", "LOGIN OK", Constants.SYSTEM_LANCIA, request.getRemoteUser(), request.getRemoteAddr());

            return ResponseEntity.ok(new JwtAuthenticationResponse(token, user, list, request.getRemoteAddr()));

    }


    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser)userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            User currentUser = userRep.findByUsername(user.getUsername());
            List<Authority> list = currentUser.getAuthorities();
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, currentUser, list, request.getRemoteAddr()));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}