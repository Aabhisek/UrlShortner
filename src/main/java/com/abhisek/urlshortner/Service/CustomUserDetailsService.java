package com.abhisek.urlshortner.Service;

import com.abhisek.urlshortner.Entiry.User;
import com.abhisek.urlshortner.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    private final UserRepository userRepository;


    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User Not Found" + username));
        return new CustomUserDetails(user);
    }
}
