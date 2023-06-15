package ib.projekat.IBprojekat.websecurity;

import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.exception.UserNotActivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email '%s' not found!".formatted(username)));
        if (!user.isEnabled()) throw new UserNotActivatedException();
        return new UserDetailsImpl(user);
    }
}
