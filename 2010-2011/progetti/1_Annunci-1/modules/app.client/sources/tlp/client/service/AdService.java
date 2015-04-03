package tlp.client.service;

import java.util.List;
import tlp.client.model.AdModel;
import tlp.client.model.UserModel;

public interface AdService
{
        public AdService save(AdModel model);

        public AdService remove(AdModel.Id adId);

        public AdModel one(AdModel.Id adId);

        public List<AdModel> all(UserModel.Id userId);

        public List<AdModel> match(AdModel.Type type, AdModel.Tags tags);
}
