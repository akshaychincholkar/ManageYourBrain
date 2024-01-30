import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Memory from './memory';
import MemoryDetail from './memory-detail';
import MemoryUpdate from './memory-update';
import MemoryDeleteDialog from './memory-delete-dialog';

const MemoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Memory />} />
    <Route path="new" element={<MemoryUpdate />} />
    <Route path=":id">
      <Route index element={<MemoryDetail />} />
      <Route path="edit" element={<MemoryUpdate />} />
      <Route path="delete" element={<MemoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MemoryRoutes;
