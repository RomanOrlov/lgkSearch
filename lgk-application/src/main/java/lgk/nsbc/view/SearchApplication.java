package lgk.nsbc.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import lgk.nsbc.backend.dao.SysAgentsRepository;
import lgk.nsbc.backend.dao.SysSessionsRepository;
import lgk.nsbc.backend.entity.SysAgents;
import lgk.nsbc.backend.entity.SysSessions;
import lgk.nsbc.presenter.SearchPresenterImpl;
import lgk.nsbc.view.searchview.SearchView;
import lgk.nsbc.view.searchview.SearchViewImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
