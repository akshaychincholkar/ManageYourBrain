import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Mnemonic from './mnemonic';
import MnemonicDetail from './mnemonic-detail';
import MnemonicUpdate from './mnemonic-update';
import MnemonicDeleteDialog from './mnemonic-delete-dialog';

const MnemonicRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Mnemonic />} />
    <Route path="new" element={<MnemonicUpdate />} />
    <Route path=":id">
      <Route index element={<MnemonicDetail />} />
      <Route path="edit" element={<MnemonicUpdate />} />
      <Route path="delete" element={<MnemonicDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MnemonicRoutes;
