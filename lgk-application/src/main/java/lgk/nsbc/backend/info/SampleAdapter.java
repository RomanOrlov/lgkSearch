package lgk.nsbc.backend.info;

import lgk.nsbc.backend.dao.SamplesRepository;
import lgk.nsbc.backend.entity.sample.Sample;
import lgk.nsbc.backend.entity.sample.SampleCriteria;
import lgk.nsbc.backend.entity.sample.SampleData;
import lgk.nsbc.backend.entity.sample.SampleDisplayedInfo;
import lgk.nsbc.backend.info.criteria.Criteria;
import lgk.nsbc.backend.info.searchable.SearchTarget;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Посредник между выборкой в базе и в памяти.
 */
@Component
@Scope("prototype")
public class SampleAdapter {
    @Autowired
    private AllSearchTargets allSearchTargets;
    @Autowired
    private SamplesRepository samplesRepository;
    @Getter
    private SearchTarget searchTarget;
    @Getter
    private List<Criteria> criteriaList;
    @Getter
    private List<DisplayedInfo> displayedInfoList;

    private final Sample sample;

    public SampleAdapter() {
        sample = new Sample();
    }

    public SampleAdapter buildTemplateSample() {
        sample.setName("Новая выборка");
        sample.setComments("");
        searchTarget = allSearchTargets.getTargetByName("Пациент");
        sample.setSearchTarget("Пациент");
        refreshSearchTarget();
        return this;
    }

    @SuppressWarnings("uncheked")
    private void refreshSearchTarget() {
        criteriaList = searchTarget.getCriteriaList();
        displayedInfoList = searchTarget.getDisplayedInfoList();
    }

    public List<Criteria> getOnlySelectedCriteria() {
        return criteriaList.stream()
                .filter(Criteria::isSelected)
                .collect(toList());
    }

    public List<DisplayedInfo> getOnlySelectedDisplayInfo() {
        return displayedInfoList.stream()
                .filter(DisplayedInfo::isSelected)
                .collect(toList());
    }

    public Set<SampleData> getSampleData() {
        return sample.getSampleDataList();
    }

    public void changeSearchTarget(SearchTarget searchTarget) {
        this.searchTarget = searchTarget;
        refreshSearchTarget();
    }

    public void removeSample() {
        samplesRepository.delete(sample);
    }

    /**
     * @param displayedInfoList Новая выводимая информация
     *                          Обновляю список выводимой информации (по имени)
     */
    @Transactional
    public void refreshDisplayedInfo(List<DisplayedInfo> displayedInfoList) {
        List<SampleDisplayedInfo> refreshedDisplayInfo = displayedInfoList.stream()
                .map(displayedInfo -> new SampleDisplayedInfo(sample, displayedInfo.getRusName()))
                .collect(toList());
        sample.getSampleDisplayedInfoList().retainAll(refreshedDisplayInfo);
    }

    /**
     * @param criteriaList Новый список критериев
     *                     Тупо удаляю все критерии и выставляю новые.
     */
    @Transactional
    public void refreshCriteria(List<Criteria> criteriaList) {
        List<SampleCriteria> refreshedCriteria = criteriaList.stream()
                .map(criteria -> new SampleCriteria(sample, criteria.getRusName(), criteria.getCriteriaStringValue()))
                .collect(toList());
        sample.getSampleCriteriaList().removeAll(refreshedCriteria);
        sample.getSampleCriteriaList().addAll(refreshedCriteria);
    }

    @Transactional
    public void saveSample(String name, String comment, Integer userId) {
        sample.setName(name);
        sample.setComments(comment);
        sample.setUserId(userId);
        samplesRepository.saveAndFlush(sample);
    }

    public void deleteSelectedSampleData(List<Integer> uniqueIdentifiersSelected) {
        sample.getSampleDataList().removeIf(sampleData ->
                uniqueIdentifiersSelected.contains(sampleData.getObjectUniqueId()));
    }

    /**
     * Добавляет в выборку только новые элементы.
     *
     * @param uniqueIdentifiersSelected Уникальные ID объектов выборки
     */
    @Transactional
    public void addSelectedSampleData(List<Integer> uniqueIdentifiersSelected) {
        List<SampleData> selectedSampleData = uniqueIdentifiersSelected.stream()
                .map(uniqueId -> new SampleData(sample, uniqueId))
                .collect(toList());
        sample.getSampleDataList().addAll(selectedSampleData);
    }

    public SampleAdapter parseSample(Sample sample) {
        searchTarget = allSearchTargets.getTargetByName(sample.getSearchTarget());
        refreshSearchTarget();
        Map<String, Criteria> criteriaMap = criteriaList.stream()
                .collect(Collectors.toMap(Criteria::getRusName, criteria -> criteria));
        Map<String, DisplayedInfo> displayedInfoMap = displayedInfoList.stream()
                .collect(Collectors.toMap(DisplayedInfo::getRusName, displayedInfo -> displayedInfo));
        sample.getSampleCriteriaList().forEach(sampleCriteria -> {
            Criteria criteria = criteriaMap.get(sampleCriteria.getDisplayedInfoName());
            criteria.setSelected(true);
            criteria.parseCriteriaStringValue(sampleCriteria.getValue());
        });
        sample.getSampleDisplayedInfoList().forEach(sampleDisplayedInfo -> {
            DisplayedInfo displayedInfo = displayedInfoMap.get(sampleDisplayedInfo.getDisplayedInfoName());
            displayedInfo.setSelected(true);
        });
        return this;
    }

    @Override
    public String toString() {
        return sample.getName();
    }
}

