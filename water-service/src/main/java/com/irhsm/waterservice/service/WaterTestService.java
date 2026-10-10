package com.irhsm.waterservice.service;

import com.irhsm.waterservice.dto.WaterTestRequest;
import com.irhsm.waterservice.entity.WaterQualityTest;
import com.irhsm.waterservice.entity.WaterSource;
import com.irhsm.waterservice.enums.TestResult;
import com.irhsm.waterservice.repository.WaterQualityTestRepository;
import com.irhsm.waterservice.repository.WaterSourceRepository;
import com.irhsm.waterservice.rules.WaterQualityStandard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterTestService {

    private final WaterQualityTestRepository testRepository;
    private final WaterSourceRepository sourceRepository;
    private final IssueService issueService;

    public List<WaterQualityTest> findAll(Long sourceId) {
        return (sourceId == null)
                ? testRepository.findAllByOrderByTestDateDesc()
                : testRepository.findBySourceIdOrderByTestDateDesc(sourceId);
    }

    @Transactional
    public WaterQualityTest create(WaterTestRequest req) {
        WaterSource source = sourceRepository.findById(req.sourceId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Water source " + req.sourceId() + " not found"));

        List<String> failed = WaterQualityStandard.failedParameters(req);

        WaterQualityTest test = new WaterQualityTest();
        test.setSource(source);
        test.setTestDate(req.testDate());
        test.setTestedBy(req.testedBy());
        test.setPh(req.ph());
        test.setTds(req.tds());
        test.setResidualChlorine(req.residualChlorine());
        test.setTurbidity(req.turbidity());
        test.setColiformPresent(req.coliformPresent());
        test.setResult(failed.isEmpty() ? TestResult.SAFE : TestResult.UNSAFE);
        test.setFailedParameters(failed.isEmpty() ? null : String.join(", ", failed));

        WaterQualityTest saved = testRepository.save(test);

        // Rule: a bad result never just sits in a table, it opens an issue for the officer
        if (saved.getResult() == TestResult.UNSAFE) {
            issueService.openFromUnsafeTest(saved);
        }
        return saved;
    }
}