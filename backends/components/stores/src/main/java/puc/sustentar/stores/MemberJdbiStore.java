package puc.sustentar.stores;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import puc.sustentar.common.PasswordHelper;
import puc.sustentar.entities.Member;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static puc.sustentar.common.PasswordHelper.decodeUsingGuava;
import static puc.sustentar.common.PasswordHelper.makePwHash;

public class MemberJdbiStore extends JdbiStore implements MemberStore {

    public MemberJdbiStore(Handle handle) {
        super(handle);
    }

    @Inject
    public MemberJdbiStore(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public Member getMemberByEmail(String email) {
        return withHandle(h -> h.createQuery(
                "select * "
                        + "from members "
                        + "where email = :email "
                        + "limit 1")
                .bind("email", email)
                .mapToBean(Member.class)
                .findOnly()
        );
    }

    @Override
    public void checkPassword(String password, Member member){

        PasswordHelper.SaltAndHash snh = null;
        try {
            snh = makePwHash(password, decodeUsingGuava(member.getSalt()));
        } catch (NoSuchAlgorithmException e) {
            throw new WrongPasswordException(e);
        } catch (InvalidKeySpecException e) {
            throw new WrongPasswordException(e);
        }

        if( !snh.hash.equals(member.getPwHash())){
            throw new WrongPasswordException();
        }
    }


}
