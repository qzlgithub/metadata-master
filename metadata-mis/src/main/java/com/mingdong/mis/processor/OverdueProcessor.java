package com.mingdong.mis.processor;

import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.metadata.OverdueBO;
import com.mingdong.mis.model.vo.PhoneVO;
import org.springframework.stereotype.Component;

@Component
public class OverdueProcessor implements IProcessor<PhoneVO>
{
//    @Resource
//    private OverdueUserRepository overdueUserRepository;
//    @Resource
//    private OverduePlatformRepository overduePlatformRepository;

    @Override
    public Metadata<OverdueBO> process(PhoneVO payload)
    {
        Metadata<OverdueBO> metadata = new Metadata<>();
//        OverdueUser overdueUser = overdueUserRepository.findByPhone(payload.getPhone());
//        if(overdueUser != null)
//        {
//            OverdueBO bo = new OverdueBO();
//            bo.setOverdueAmountMax(overdueUser.getOverdueAmountMax());
//            bo.setOverdueDaysMax(overdueUser.getOverdueDaysMax());
//            bo.setOverdueEarliestTime(overdueUser.getOverdueEarliestTime());
//            bo.setOverdueLatestTime(overdueUser.getOverdueLatestTime());
//            bo.setOverduePlatformToday(overdueUser.getOverduePlatformToday());
//            bo.setOverduePlatformTotal(overdueUser.getOverduePlatformTotal());
//            bo.setOverduePlatform3Days(overdueUser.getOverduePlatform3Days());
//            bo.setOverduePlatform7Days(overdueUser.getOverduePlatform7Days());
//            bo.setOverduePlatform15Days(overdueUser.getOverduePlatform15Days());
//            bo.setOverduePlatform30Days(overdueUser.getOverduePlatform30Days());
//            bo.setOverduePlatform60Days(overdueUser.getOverduePlatform60Days());
//            bo.setOverduePlatform90Days(overdueUser.getOverduePlatform90Days());
//            List<OverduePlatform> opList = overduePlatformRepository.findByPhone(payload.getPhone());
//            if(!CollectionUtils.isEmpty(opList))
//            {
//                List<OverduePlatformBO> list = new ArrayList<>(opList.size());
//                for(OverduePlatform o : opList)
//                {
//                    OverduePlatformBO op = new OverduePlatformBO();
//                    op.setPlatformCode(o.getPlatformCode());
//                    op.setPlatformType(o.getPlatformType());
//                    op.setOverdueEarliestTime(o.getOverdueEarliestTime());
//                    op.setOverdueLatestTime(o.getOverdueLatestTime());
//                    list.add(op);
//                }
//                bo.setOverduePlatforms(list);
//            }
//            metadata.setHit(true);
//            metadata.setData(bo);
//        }
        return metadata;
    }
}
