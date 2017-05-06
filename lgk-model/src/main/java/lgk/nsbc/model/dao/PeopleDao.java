package lgk.nsbc.model.dao;

import lgk.nsbc.generated.tables.records.BasPeopleRecord;
import lgk.nsbc.model.People;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;

@Service
public class PeopleDao implements Serializable{
    @Autowired
    private DSLContext context;

    public List<People> getPeoplesBySurname(Set<String> surname) {
        Result<BasPeopleRecord> fetch = context.fetch(BAS_PEOPLE, BAS_PEOPLE.SURNAME.in(surname));
        return fetch.stream()
                .map(People::buildFromRecord)
                .collect(Collectors.toList());
    }
}
