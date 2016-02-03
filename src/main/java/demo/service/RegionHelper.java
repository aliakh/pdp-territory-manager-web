package demo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import demo.domain.Conflict;
import demo.domain.Coord;
import demo.domain.RegionResponse;
import demo.domain.RegionZip;
import demo.domain.entity.Franchise;
import demo.domain.entity.Region;
import demo.domain.entity.Zip;
import demo.domain.entity.ZipType;
import demo.mapper.RegionMapper;
import demo.repository.FranchiseRepository;
import demo.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Region helper.
 */
@Component
public class RegionHelper {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Autowired
    private RegionMapper regionMapper;

    private int franchiseId;
    private int radius;
    private boolean test;

    private static final String REGION_IS_CREATED = "Region %d is created";
    private static final String REGION_IS_UPDATED = "Region %d is updated";
    private static final String PREVIEW = " (preview)";
    private static final String ZIP = "ZIP %s: %s";

    private static final Set<ZipType> ACTIVE_ZIP_TYPES = Sets.newHashSet(ZipType.NEW, ZipType.WON, ZipType.HOME_PINNED);

    public RegionResponse createOrUpdateRegion(int franchiseIdParam, int radiusParam, boolean testParam) {
        this.franchiseId = franchiseIdParam;
        this.radius = radiusParam;
        this.test = testParam;

        Franchise franchise = franchiseRepository.findOne(franchiseId);
        Assert.notNull(franchise);
        Region region = getRegion(franchise);

        Map<Zip, List<Conflict>> issues = createIssues();
        addConflictsToIssues(franchise.getMake(), issues);
        Coord coord = franchise.getCoord();

        for (Zip zip : issues.keySet()) {
            onZip(zip, issues, region.getRegionId(), coord);
        }

        Collection<Zip> zips = setPinnedZip(issues.keySet(), franchise);
        saveRegion(region, zips);

        return new RegionResponse(
                franchiseId,
                radius,
                test,
                franchiseRepository.findOne(franchiseId),
                Lists.newArrayList(zips),
                getMessages(region, zips));
    }

    private Region getRegion(Franchise franchise) {
        Region region = regionRepository.findByFranchise_FranchiseId(franchiseId);
        if (region != null) {
            updateRegion(region, franchise);
            region.setCreated(false);
            if (!test) {
                regionMapper.deleteAllRegionZips(region.getRegionId());
            }
        } else {
            region = createRegion(franchise);
            region.setCreated(true);
        }
        return region;
    }

    private Region createRegion(Franchise franchise) {
        Region region = new Region();
        region.setRegionId(getNextRegionId());
        updateRegion(region, franchise);
        return region;
    }

    private Region updateRegion(Region region, Franchise franchise) {
        region.setRegionCode(franchise.getState() + franchise.getZipCode());
        region.setRegionDescription(getRegionDescription(franchise.getFranchiseName()));
        region.setRegionMake(franchise.getMake());
        region.setState(franchise.getState());
        region.setCoord(franchise.getCoord());
        region.setFranchise(franchise);
        region.setRadius(radius);
        return region;
    }

    private int getNextRegionId() {
        Integer regionId = regionMapper.getMaxRegionId();
        return regionId == null ? 1 : regionId + 1;
    }

    private String getRegionDescription(String franchiseName) {
        return franchiseName.length() < 100 ? franchiseName : franchiseName.substring(0, 100);
    }

    private Map<Zip, List<Conflict>> createIssues() {
        List<Zip> zipsAll = regionMapper.getAllZips(franchiseId, radius);
        Map<Zip, List<Conflict>> issues = Maps.newLinkedHashMap();
        for (Zip zip : zipsAll) {
            issues.put(zip, Lists.<Conflict>newArrayList());
        }
        return issues;
    }

    private void addConflictsToIssues(String make, Map<Zip, List<Conflict>> issues) {
        List<Conflict> conflictsAll = regionMapper.getConflicts(franchiseId, radius, make);
        for (Conflict conflict : conflictsAll) {
            List<Conflict> conflicts = issues.get(conflict.getZip());
            if (conflicts == null) {
                conflicts = Lists.newArrayList();
                issues.put(conflict.getZip(), conflicts);
            }
            conflicts.add(conflict);
        }
    }

    private void onZip(Zip zip, Map<Zip, List<Conflict>> issues, int regionId, Coord coord) {
        List<String> messages = Lists.newArrayList();

        List<Conflict> conflicts = issues.get(zip);
        if (conflicts.isEmpty()) {
            zip.setZipType(ZipType.NEW);
        } else {
            zip.setZipType(ZipType.WON);
            for (Conflict conflict : conflicts) {
                onConflict(zip, conflict, regionId, coord, messages);
            }
        }

        if (zip.getZipType() == ZipType.NEW) {
            messages.add(ZipType.NEW.getMessage());
        }
        zip.setMessages(messages);
    }

    private void onConflict(Zip zip, Conflict conflict, int regionId, Coord coord, List<String> messages) {
        int conflictRegionId = conflict.getRegion().getRegionId();
        if (conflict.isPinned()) {
            zip.setZipType(ZipType.LOST_PINNED);
            messages.add(String.format(ZipType.LOST_PINNED.getMessage(), conflictRegionId));
        } else {
            if (regionId == conflictRegionId) {
                zip.setZipType(ZipType.OWN);
                messages.add(ZipType.OWN.getMessage());
            } else {
                double distance1 = coord.getDistance(zip.getCoord()).doubleValue();
                double distance2 = conflict.getRegion().getCoord().getDistance(zip.getCoord()).doubleValue();

                if ((distance1 > distance2) && (Math.abs(distance1 - distance2) >= 0.01)) {
                    zip.setZipType(ZipType.LOST);
                    if (!test) {
                        regionMapper.deleteRegionZip(conflictRegionId, conflict.getZip().getZipCode());
                    }
                    messages.add(String.format(ZipType.LOST.getMessage(), conflictRegionId, distance2, distance1));
                } else {
                    messages.add(String.format(ZipType.WON.getMessage(), conflictRegionId, distance2, distance1));
                }
            }
        }
    }

    private Collection<Zip> setPinnedZip(Collection<Zip> zips, Franchise franchise) {
        Zip zip = findPinnedZipCandidate(zips, franchise.getZipCode());
        if (zip == null) {
            zip = regionMapper.getZip(franchise.getZipCode());

            RegionZip regionZip = regionMapper.getRegionZip(franchise.getMake(), zip.getZipCode());
            if (!test) {
                if (regionZip != null && !regionZip.isPinned()) {
                    regionMapper.deleteRegionZip(regionZip.getRegionId(), zip.getZipCode());
                }
            }

            zips.add(zip);
        }

        zip.setZipType(ZipType.HOME_PINNED);
        zip.addMessage(ZipType.HOME_PINNED.getMessage());
        return zips;
    }

    private Zip findPinnedZipCandidate(Collection<Zip> zips, String zipCode) {
        for (Zip zip : zips) {
            if (zip.getZipCode().equals(zipCode)) {
                return zip;
            }
        }
        return null;
    }

    private void saveRegion(Region region, Collection<Zip> zips) {
        if (!test) {
            if (region.isCreated()) {
                regionMapper.insertRegion(region);
            } else {
                regionMapper.updateRegion(region);
            }

            for (Zip zip : zips) {
                if (ACTIVE_ZIP_TYPES.contains(zip.getZipType())) {
                    boolean pinned = zip.getZipType() == ZipType.HOME_PINNED;
                    regionMapper.insertRegionZip(region.getRegionId(), zip.getZipCode(), pinned);
                }
            }
        }
    }

    private List<String> getMessages(Region region, Collection<Zip> zips) {
        List<String> messages = Lists.newArrayList();
        String message = String.format(region.isCreated() ? REGION_IS_CREATED : REGION_IS_UPDATED, region.getRegionId());
        if (test) {
            message += PREVIEW;
        }
        messages.add(message);
        for (Zip zip : zips) {
            for (String text : zip.getMessages()) {
                messages.add(String.format(ZIP, zip.getZipCode(), text));
            }
        }
        return messages;
    }
}
