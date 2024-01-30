import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AppUser from './app-user';
import Tag from './tag';
import Mnemonic from './mnemonic';
import Memory from './memory';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="app-user/*" element={<AppUser />} />
        <Route path="tag/*" element={<Tag />} />
        <Route path="mnemonic/*" element={<Mnemonic />} />
        <Route path="memory/*" element={<Memory />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
