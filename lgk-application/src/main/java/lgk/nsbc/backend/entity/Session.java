package lgk.nsbc.backend.entity;

/**
 * Сессия (авторизация)
 */
public class Session {

    private String n;
    private String login;
    private Long DeafultOrgN;
    private String name;
    private String surname;

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getDeafultOrgN() {
        return DeafultOrgN;
    }

    public void setDeafultOrgN(Long deafultOrgN) {
        DeafultOrgN = deafultOrgN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getNLongValue() {
        return Long.valueOf(n);
    }
}
