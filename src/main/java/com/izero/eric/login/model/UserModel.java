package com.izero.eric.login.model;

import java.util.Collection;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserModel  implements UserDetails{

    @NotBlank(message = "帳號不可為空")
    private String id;
    private String name;
  
	/*@Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-zA-Z]{1}[a-zA-Z0-9]{5,15}$", 
	 		 message = "密碼必須為6 至16 位英文及數字組成且首位字元為英文。")*/
    @NotBlank(message = "密碼不可為空")
    private String pwd;
    private String isuse;
    private String inpid;
    private String inptime;

    public UserModel(){
        this.id = null;
    }

	public UserModel(String id,String pwd,Collection<? extends GrantedAuthority> auths){
        this.id = id;
		this.pwd = pwd;
		this.authorities = auths;
    }

    private Collection<? extends GrantedAuthority> authorities;

    @Override
	// 取得所有權限
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.authorities;
	}

	@Override
	// 取得使用者名稱
	public String getUsername() {

		return id;
	}

	@Override
	// 取得密碼
	public String getPassword() {
		return pwd;
	}
	
	@Override
	// 帳號是否過期
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	// 帳號是否被鎖定
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	// 憑證/密碼是否過期
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	// 帳號是否可用
	public boolean isEnabled() {

		return true;
	}
}
