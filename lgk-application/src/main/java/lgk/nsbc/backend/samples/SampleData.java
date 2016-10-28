package lgk.nsbc.backend.samples;

/**
 * Bean для данных выборки
 */
public class SampleData {
    private Long n;
    private Long selection_id;
    private Long unique_id;

    public SampleData(Long selection_id, Long unique_id) {
        this.selection_id = selection_id;
        this.unique_id = unique_id;
    }

    public SampleData() {
    }

    public SampleData(Long n, Long selection_id, Long unique_id) {
        this.n = n;
        this.selection_id = selection_id;
        this.unique_id = unique_id;
    }

    public Long getSelection_id() {
        return selection_id;
    }

    public void setSelection_id(Long selection_id) {
        this.selection_id = selection_id;
    }

    public Long getN() {
        return n;
    }

    public void setN(Long n) {
        this.n = n;
    }

    public Long getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(Long unique_id) {
        this.unique_id = unique_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null||!(obj instanceof SampleData)) {
            return false;
        }
        SampleData sampleData = (SampleData)obj;
        // 2 объекта выборка эквиваленты, если они принадлежат одной выборке и обладают одинаковым Id
        return this.getSelection_id().equals(sampleData.getSelection_id())&&
                this.getUnique_id().equals(sampleData.getUnique_id());
    }
}
