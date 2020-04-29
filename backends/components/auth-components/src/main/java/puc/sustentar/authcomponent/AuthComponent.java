package puc.sustentar.authcomponent;

import puc.sustentar.entities.Member;
import puc.sustentar.stores.MemberStore;

import javax.inject.Inject;

public class AuthComponent {
    MemberStore memberStore;


    @Inject
    public AuthComponent(MemberStore memberStore) {
        this.memberStore = memberStore;
    }


    public  LoggedIn login(String email, String password, TokenInterop tokenInterop){
        Member member= memberStore.getMemberByEmail(email);
        memberStore.checkPassword(password, member);
        String token = tokenInterop.createSessionToken(email);
        return new LoggedIn(token);
    }

}
