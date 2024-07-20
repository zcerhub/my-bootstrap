package com.base.sys.auth;

import com.base.sys.api.dto.AuthenticationUserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
  *@author keriezhang
  * @date 2021/04/15
 */
public class AuthenticationUser  implements UserDetails {
    private String password;
    private String username;
    private AuthenticationUserDto authenticationUserDto;

    public AuthenticationUser(String username, String password, AuthenticationUserDto authenticationUserDto) {
        if (username != null && !"".equals(username) && password != null) {
            this.username = username;
            this.password = password;
            this.authenticationUserDto = authenticationUserDto;
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }



    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        private static final long serialVersionUID = 530L;

        private AuthorityComparator() {
        }

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            } else {
                return g1.getAuthority() == null ? 1 : g1.getAuthority().compareTo(g2.getAuthority());
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public AuthenticationUserDto getAuthenticationUserDto() {
        return authenticationUserDto;
    }
}

