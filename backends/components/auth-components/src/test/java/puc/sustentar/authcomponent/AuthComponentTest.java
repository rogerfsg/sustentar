package puc.sustentar.authcomponent;

import org.junit.Test;
import puc.sustentar.entities.Member;
import puc.sustentar.stores.MemberStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class AuthComponentTest {
    @Test
    public void loginShouldWorks(){
        MemberStore memberStore = mock(MemberStore.class);
        Member member = mock(Member.class);
        TokenInterop tokenInterop = mock(TokenInterop.class);
        String email = "rogerfsg@gmail.com";
        String password = "123456";
        String token = "123";
        when(memberStore.getMemberByEmail(email)).thenReturn(member);
        when(member.getEmail()).thenReturn(email);
        when(tokenInterop.createSessionToken(email)).thenReturn(token);
        AuthComponent authComponent = new AuthComponent(memberStore);
        LoggedIn loggedIn = authComponent.login(email, password, tokenInterop);

        assertNotNull(loggedIn);
        assertEquals(token, loggedIn.getToken());
        verify(tokenInterop).createSessionToken(email);
        verify(memberStore).getMemberByEmail(email);
        verify(memberStore).checkPassword(password, member);
        verifyNoMoreInteractions(memberStore, member);
    }
}
