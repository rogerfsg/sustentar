package puc.sustentar.stores;

import puc.sustentar.entities.Member;

public interface MemberStore {

    Member getMemberByEmail(String email);

    void checkPassword(String password, Member member);
}
