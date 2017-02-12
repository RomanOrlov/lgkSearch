package lgk.nsbc.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import lgk.nsbc.DBConnectionProperty;
import lgk.nsbc.view.searchview.SearchViewImpl;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

@Title("DB interface")
@Theme("valo")
@SpringUI
public class SearchApplication extends UI {
    @Autowired
    private SearchViewImpl searchView;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        String lgkSessionId = getPage().getUriFragment();
        // To change key add #9ec2104e97256dc639754ae07b5eb7bf
        if (lgkSessionId == null) {
            lgkSessionId = "9ec2104e97256dc639754ae07b5eb7bf";
        }
        //LoginForm loginForm = new LoginForm();
        getPage().setUriFragment(null, false);
        /*SysSessions bySID = sysSessionsRepository.findBySid(lgkSessionId);
        System.out.println(bySID);*/
        TabSheet tabs = new TabSheet();
        setContent(tabs);
        tabs.setSizeFull();
        tabs.addTab(searchView, "Построение выборок");
        // Планируемый раздел для поиска внутри памяти
        tabs.addTab(new Label("Пока здесь ничего нет"), "Работа с выборками");
    }
}
