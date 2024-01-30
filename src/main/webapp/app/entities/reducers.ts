import appUser from 'app/entities/app-user/app-user.reducer';
import tag from 'app/entities/tag/tag.reducer';
import mnemonic from 'app/entities/mnemonic/mnemonic.reducer';
import memory from 'app/entities/memory/memory.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  appUser,
  tag,
  mnemonic,
  memory,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
