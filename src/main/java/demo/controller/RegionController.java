package demo.controller;

import demo.domain.FranchiseResponse;
import demo.domain.JqgridResponse;
import demo.domain.RegionResponse;
import demo.domain.ZipsResponse;
import demo.domain.entity.Region;
import demo.domain.search.Filter;
import demo.domain.search.Page;
import demo.domain.search.Search;
import demo.domain.search.Sort;
import demo.service.FranchiseService;
import demo.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Region controller.
 */
@Controller
@RequestMapping()
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private FranchiseService franchiseService;

    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    public
    @ResponseBody
    JqgridResponse<Region> getRegions(
            @RequestParam("_search") boolean search,
            @RequestParam(value = "searchField", required = false) String searchField,
            @RequestParam(value = "searchString", required = false) String searchString,
            @RequestParam(value = "searchOper", required = false) String searchOper,
            @RequestParam("rows") int rows,
            @RequestParam("page") int page,
            @RequestParam("sidx") String sidx,
            @RequestParam("sord") String sord) {

        Filter filter = new Filter(
                new Page(page, rows),
                new Sort(sidx, sord),
                new Search(searchField, searchString, searchOper)
        );
        List<Region> regions = regionService.findAll(filter);

        JqgridResponse<Region> response = new JqgridResponse<Region>();
        response.setRows(regions);
        response.setMax(rows);
        response.setPage(page);
        response.setTotal(regionService.count());
        return response;
    }

    @RequestMapping(value = "/region", method = RequestMethod.GET)
    public
    @ResponseBody
    Region getRegion(@RequestParam("regionId") int regionId) {
        return regionService.find(regionId);
    }

    @RequestMapping(value = "/zips", method = RequestMethod.GET)
    public
    @ResponseBody
    ZipsResponse getAllZips(
            @RequestParam("franchiseId") int franchiseId,
            @RequestParam("radius") int radius) {

        return new ZipsResponse(
                franchiseId,
                radius,
                franchiseService.find(franchiseId),
                regionService.getAllZips(franchiseId, radius)
        );
    }

    @RequestMapping(value = "/regions", method = RequestMethod.POST)
    public
    @ResponseBody
    RegionResponse createOrUpdateRegion(
            @RequestParam("franchiseId") int franchiseId,
            @RequestParam("radius") int radius,
            @RequestParam("test") boolean test) {

        return regionService.createOrUpdateRegion(franchiseId, radius, test);
    }

    @RequestMapping(value = "/franchises", method = RequestMethod.GET)
    public
    @ResponseBody
    List<FranchiseResponse> getFranchises(@RequestParam("term") String term) {
        return franchiseService.findFranchisesNameLike(term);
    }
}
