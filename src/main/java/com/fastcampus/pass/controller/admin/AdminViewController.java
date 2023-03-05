package com.fastcampus.pass.controller.admin;

import com.fastcampus.pass.service.packaze.PackageService;
import com.fastcampus.pass.service.pass.BulkPassService;
import com.fastcampus.pass.service.statistics.StatisticsService;
import com.fastcampus.pass.service.user.UserGroupMappingService;
import com.fastcampus.pass.util.LocalDateTimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "/admin")
public class AdminViewController {
    private final BulkPassService bulkPassService;
    private final PackageService packageService;
    private final UserGroupMappingService userGroupMappingService;
    private final StatisticsService statisticsService;


    public AdminViewController(BulkPassService bulkPassService, PackageService packageService, UserGroupMappingService userGroupMappingService, StatisticsService statisticsService) {        this.bulkPassService = bulkPassService;
        this.packageService = packageService;
        this.userGroupMappingService = userGroupMappingService;
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ModelAndView home(ModelAndView modelAndView, @RequestParam("to") String toString) {
        LocalDateTime to = LocalDateTimeUtils.parseDate(toString);

        // chartData를 조회합니다. (라벨, 출석횟수, 취소 횟수)
        modelAndView.addObject("chartData", statisticsService.makeChartData(to));
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }

    @GetMapping("/bulk-pass")
    public ModelAndView registerBulkPass(ModelAndView modelAndView) {
        // bulk pass 목록을 조회합니다.
        modelAndView.addObject("bulkPasses", bulkPassService.getAllBulkPasses());
        // bulk pass를 등록할 때 필요한 package 값을 위해 모든 package를 조회합니다.
        modelAndView.addObject("packages", packageService.getAllPackages());
        // bulk pass를 등록할 때 필요한 userGroupId 값을 위해 모든 userGroupId를 조회합니다.
        modelAndView.addObject("userGroupIds", userGroupMappingService.getAllUserGroupIds());
        // bulk pass request를 제공합니다.
        modelAndView.addObject("request", new BulkPassRequest());
        modelAndView.setViewName("admin/bulk-pass");

        return modelAndView;
    }

    @PostMapping("/bulk-pass")
    public String addBulkPass(@ModelAttribute("request") BulkPassRequest request, Model model) {
        // bulk pass 를 생성합니다.
        bulkPassService.addBulkPass(request);
        return "redirect:/admin/bulk-pass";
    }
}