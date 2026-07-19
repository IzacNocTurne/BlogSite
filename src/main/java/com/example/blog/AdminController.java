package com.example.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PostService postService;

    @GetMapping("/stats")
    public String getStats(Model model) {
        List<ReferralStats> referralStats = postService.getReferralStats();
        model.addAttribute("referralStats", referralStats);

        List<String> labels = referralStats.stream().map(ReferralStats::getSearchEngine).collect(Collectors.toList());
        List<Long> data = referralStats.stream().map(ReferralStats::getCount).collect(Collectors.toList());

        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartData", data);

        model.addAttribute("topPosts", postService.getTop5ReferredPosts());
        return "admin-stats";
    }

    @GetMapping("/stats/clear")
    public String clearStats() {
        postService.clearStats();
        return "redirect:/admin/stats";
    }
}
