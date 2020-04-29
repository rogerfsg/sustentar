package puc.sustentar.stores;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import puc.sustentar.entities.Member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MemberJdbiStoreTest {


    @Rule
    public JdbiResource jdbi = new JdbiResource();

    private MemberStore memberStore;

    @Before
    public void setUp() {
        memberStore = new MemberJdbiStore(jdbi.getHandle());
    }

    @Test
    public void getMemberByEmailShouldWorks(){
        String email = "rogerfsg@gmail.com";
        Member member= memberStore.getMemberByEmail(email);
        assertNotNull(member);
        assertEquals(email, member.getEmail());
    }

    @Test
    public void checkPasswordShouldWorks(){
        Member member = memberStore.getMemberByEmail("rogerfsg@gmail.com");
        memberStore.checkPassword("123456", member);
    }

    @Test
    public void checkPasswordShouldntWorkWithIncorrectPassword(){
        try {
            Member member = memberStore.getMemberByEmail("rogerfsg@gmail.com");
            memberStore.checkPassword("1133", member);
        } catch (WrongPasswordException e){

        }
    }

}
