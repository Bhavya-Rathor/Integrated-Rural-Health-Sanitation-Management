package com.irhsm.healthCampService.service;

import com.irhsm.healthCampService.dto.ScreeningForm;
import com.irhsm.healthCampService.entity.*;
import com.irhsm.healthCampService.repository.FollowUpRepository;
import com.irhsm.healthCampService.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

// Recording a screening. If the result is not NORMAL, a follow-up is created automatically.
@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final FollowUpRepository followUpRepository;
    private final BeneficiaryService beneficiaryService;

    public List<Screening> findByCamp(Long campId) {
        return screeningRepository.findByBeneficiaryCampIdOrderByScreenedAtDesc(campId);
    }

    public List<Screening> findByResident(Long residentId) {
        return screeningRepository.findByBeneficiaryResidentId(residentId);
    }

    // @Transactional: the screening, the attendance change and the follow-up
    // are saved together, or none of them if something fails.
    @Transactional
    public Screening record(Long campId, ScreeningForm form) {
        CampBeneficiary b = beneficiaryService.getById(form.getBeneficiaryId());
        if (!b.getCamp().getId().equals(campId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This resident is not registered for this camp");
        }

        Screening s = new Screening();
        s.setBeneficiary(b);
        s.setScreeningType(form.getScreeningType());
        s.setReading(form.getReading().trim());
        s.setResult(form.getResult());
        s.setScreenedBy(form.getScreenedBy());
        s.setRemarks(form.getRemarks());
        s = screeningRepository.save(s);

        // Being screened means the resident attended
        if (b.getAttendance() != Attendance.ATTENDED) {
            beneficiaryService.markAttendance(b.getId(), Attendance.ATTENDED);
        }

        // Not normal -> follow-up for the health officer (shows on the dashboard)
        if (form.getResult() != ScreeningResult.NORMAL) {
            createFollowUp(s);
        }
        return s;
    }

    // CRITICAL -> HIGH priority, refer to health centre within 2 days
    // ABNORMAL -> MEDIUM priority, recheck within 7 days
    private void createFollowUp(Screening s) {
        boolean critical = s.getResult() == ScreeningResult.CRITICAL;
        CampBeneficiary b = s.getBeneficiary();

        FollowUp f = new FollowUp();
        f.setScreening(s);
        f.setResidentId(b.getResidentId());
        f.setResidentName(b.getResidentName());
        f.setVillage(b.getVillage());
        f.setAction(critical ? FollowUpAction.REFER_TO_CENTRE : FollowUpAction.RECHECK);
        f.setPriority(critical ? Priority.HIGH : Priority.MEDIUM);
        f.setDueDate(LocalDate.now().plusDays(critical ? 2 : 7));
        f.setStatus(FollowUpStatus.PENDING);
        followUpRepository.save(f);
    }

    public long countAll() {
        return screeningRepository.count();
    }

    public long countNotNormal() {
        return screeningRepository.countByResultNot(ScreeningResult.NORMAL);
    }
}
