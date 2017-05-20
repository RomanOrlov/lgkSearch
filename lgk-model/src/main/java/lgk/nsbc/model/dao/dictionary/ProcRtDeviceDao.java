package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.ProcRtDevice;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcProcRtDevice.NBC_PROC_RT_DEVICE;

@Service
public class ProcRtDeviceDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, ProcRtDevice> procRtDeviceMap;

    @PostConstruct
    void init() {
        procRtDeviceMap = Collections.unmodifiableMap(context.fetch(NBC_PROC_RT_DEVICE)
                .stream()
                .map(ProcRtDevice::buildFromRecord)
                .collect(toMap(ProcRtDevice::getN, identity()))
        );
    }

    public static Map<Long, ProcRtDevice> getProcRtDeviceMap() {
        return procRtDeviceMap;
    }
}
