package lgk.nsbc.backend;

import lgk.nsbc.generated.tables.*;
import org.jooq.Condition;
import org.jooq.Table;
import org.springframework.stereotype.Service;

import java.util.IdentityHashMap;

/**
 * Класс, в котором содержится информация о структуре базы данных. А именно то, как соединить две таблицы.
 * В случае, если запрос содержит таблицу, о которой нет информации,будет кинуто исключение.
 *
 * Возможно, следует хранить информацию о структуре базы данных в одной из таблиц базы.
 * Created by Роман on 15.05.2016.
 */
@Service
public class DBStructureInfo {

    // Словарь для JOIN к таблице PATIENTS
    IdentityHashMap<Table,Condition> joinToPatientsTable = new IdentityHashMap<>();

    // Словарь для JOIN к таблице PROCEDURES
    IdentityHashMap<Table,Condition> joinToProceduresTable = new IdentityHashMap<>();

    // Словарь для JOIN к таблице MARKS
    IdentityHashMap<Table,Condition> joinToMarksTable = new IdentityHashMap<>();

    // Словарь для JOIN к таблице FOLLOWUPS
    IdentityHashMap<Table,Condition> joinToFollowUpsTable = new IdentityHashMap<>();

    // Вручную инициализируем список для JOIN
    {
        // CЛОВАРИ
        // NBC_PATIENTS <-> NBC_PATIENTS_DIAGNOSIS (DICTIONARY)
        joinToPatientsTable.put(NbcPatientsDiagnosis.NBC_PATIENTS_DIAGNOSIS,
                NbcPatients.NBC_PATIENTS.DIAGNOSIS.eq(NbcPatientsDiagnosis.NBC_PATIENTS_DIAGNOSIS.N));
        // NBC_PATIENTS <-> NBC_DIAG_2015 (DICTIONARY)
        joinToPatientsTable.put(NbcDiag_2015.NBC_DIAG_2015,
                NbcPatients.NBC_PATIENTS.NBC_DIAG_2015_N.eq(NbcDiag_2015.NBC_DIAG_2015.N));
        // NBC_PATIENTS <-> NBC_ORGANIZATIONS (DICTIONARY)
        joinToPatientsTable.put(NbcOrganizations.NBC_ORGANIZATIONS,
                NbcPatients.NBC_PATIENTS.NBC_ORGANIZATIONS_N.eq(NbcOrganizations.NBC_ORGANIZATIONS.N));

        // NBC_PATIENTS <-> BAS_PEOPLE
        joinToPatientsTable.put(BasPeople.BAS_PEOPLE,
                NbcPatients.NBC_PATIENTS.BAS_PEOPLE_N.eq(BasPeople.BAS_PEOPLE.N));
        joinToPatientsTable.put(NbcProc.NBC_PROC,
                NbcPatients.NBC_PATIENTS.N.eq(NbcProc.NBC_PROC.NBC_PATIENTS_N));
        // NBC_PATIENTS <-> NBC_TARGET
        joinToPatientsTable.put(NbcTarget.NBC_TARGET,
                NbcPatients.NBC_PATIENTS.N.eq(NbcTarget.NBC_TARGET.NBC_PATIENTS_N));

    }


    /**
     * @param target Цель поиска, которой соотвествует определенная таблица
     * @param table Таблица, для которой необходимо найти условие для JOIN
     * @return условие для JOIN(table).on(CONDITION)
     * @throws IllegalArgumentException
     */
    public Condition getJoinCondition(Target target, Table table) {
        switch (target) {
            case PATIENT:
                return joinToPatientsTable.get(table);
            case PROCEDURE:
                return joinToProceduresTable.get(table);
            case MARK:
                return joinToMarksTable.get(table);
            case FOLLOWUP:
                return joinToFollowUpsTable.get(table);
        }
        throw new IllegalArgumentException("No info about table"+table.getName());
    }
}
