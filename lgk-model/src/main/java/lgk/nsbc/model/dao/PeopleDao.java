package lgk.nsbc.model.dao;

import lgk.nsbc.generated.tables.records.PeopleRecord;
import lgk.nsbc.model.People;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.People.PEOPLE;

@Service
public class PeopleDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public List<People> getPeoplesBySurname(Set<String> surname) {
        Result<PeopleRecord> fetch = context.fetch(PEOPLE, PEOPLE.SURNAME.in(surname));
        return fetch.stream()
                .map(People::buildFromRecord)
                .collect(Collectors.toList());
    }

    public void updatePeopleObit(People people) {
        context.update(PEOPLE)
                .set(PEOPLE.OBIT, people.getObit() == null ? null : Date.valueOf(people.getObit()))
                .where(PEOPLE.N.eq(people.getN()))
                .execute();
    }
}
