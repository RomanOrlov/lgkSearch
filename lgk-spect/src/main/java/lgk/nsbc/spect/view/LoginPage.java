package lgk.nsbc.spect.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.Notification;
import lgk.nsbc.model.dao.sys.SysAgentsDao;
import lgk.nsbc.model.sys.SysAgents;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static lgk.nsbc.spect.view.MainSpectView.SPECT_VIEW;

@Service
@VaadinSessionScope
public class LoginPage extends LoginForm implements View {
    @Autowired
    private SysAgentsDao sysAgentsDao;

    public LoginPage() {
        /*
        1. Проверяем не в системе ли пользователь.
        2. Если в системе, переводим на главную страницу (ну и вытаскиваем сессию)
        3. Если не в системе, переводим на страницу авторизации, авторизуемся, регистрируем сессию.
         */
        addLoginListener(event -> {
            String username = event.getLoginParameter("username").trim();
            String password = event.getLoginParameter("password").trim();
            String pid = getPid(username, password);
            SysAgents sysAgents = sysAgentsDao.findByPid(pid);
            if (sysAgents != null && sysAgents.getName().equals(username)) {
                getUI().getNavigator().navigateTo(SPECT_VIEW);
            } else {
                Notification.show("Неверный логин/пароль",
                        "Убедитесь в правильноости ввода пароля и пользователя",
                        Notification.Type.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    private String getPid(String name, String pass) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest((name + pass).getBytes("UTF-8"));
            return Hex.encodeHexString(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
