import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAppUser } from 'app/shared/model/app-user.model';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { ITag } from 'app/shared/model/tag.model';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import { IMnemonic } from 'app/shared/model/mnemonic.model';
import { getEntities as getMnemonics } from 'app/entities/mnemonic/mnemonic.reducer';
import { IMemory } from 'app/shared/model/memory.model';
import { getEntity, updateEntity, createEntity, reset } from './memory.reducer';

export const MemoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const appUsers = useAppSelector(state => state.appUser.entities);
  const tags = useAppSelector(state => state.tag.entities);
  const mnemonics = useAppSelector(state => state.mnemonic.entities);
  const memoryEntity = useAppSelector(state => state.memory.entity);
  const loading = useAppSelector(state => state.memory.loading);
  const updating = useAppSelector(state => state.memory.updating);
  const updateSuccess = useAppSelector(state => state.memory.updateSuccess);

  const handleClose = () => {
    navigate('/memory');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getAppUsers({}));
    dispatch(getTags({}));
    dispatch(getMnemonics({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.learnedDate = convertDateTimeToServer(values.learnedDate);
    values.creationDate = convertDateTimeToServer(values.creationDate);
    values.modifiedDate = convertDateTimeToServer(values.modifiedDate);

    const entity = {
      ...memoryEntity,
      ...values,
      appUser: appUsers.find(it => it.id.toString() === values.appUser.toString()),
      tag: tags.find(it => it.id.toString() === values.tag.toString()),
      mnemonic: mnemonics.find(it => it.id.toString() === values.mnemonic.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          learnedDate: displayDefaultDateTime(),
          creationDate: displayDefaultDateTime(),
          modifiedDate: displayDefaultDateTime(),
        }
      : {
          ...memoryEntity,
          learnedDate: convertDateTimeFromServer(memoryEntity.learnedDate),
          creationDate: convertDateTimeFromServer(memoryEntity.creationDate),
          modifiedDate: convertDateTimeFromServer(memoryEntity.modifiedDate),
          appUser: memoryEntity?.appUser?.id,
          tag: memoryEntity?.tag?.id,
          mnemonic: memoryEntity?.mnemonic?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="manageYourBrainApp.memory.home.createOrEditLabel" data-cy="MemoryCreateUpdateHeading">
            <Translate contentKey="manageYourBrainApp.memory.home.createOrEditLabel">Create or edit a Memory</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="memory-id"
                  label={translate('manageYourBrainApp.memory.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('manageYourBrainApp.memory.topic')}
                id="memory-topic"
                name="topic"
                data-cy="topic"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('manageYourBrainApp.memory.learnedDate')}
                id="memory-learnedDate"
                name="learnedDate"
                data-cy="learnedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('manageYourBrainApp.memory.key')}
                id="memory-key"
                name="key"
                data-cy="key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('manageYourBrainApp.memory.comment')}
                id="memory-comment"
                name="comment"
                data-cy="comment"
                type="text"
              />
              <ValidatedField
                label={translate('manageYourBrainApp.memory.creationDate')}
                id="memory-creationDate"
                name="creationDate"
                data-cy="creationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('manageYourBrainApp.memory.modifiedDate')}
                id="memory-modifiedDate"
                name="modifiedDate"
                data-cy="modifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="memory-appUser"
                name="appUser"
                data-cy="appUser"
                label={translate('manageYourBrainApp.memory.appUser')}
                type="select"
              >
                <option value="" key="0" />
                {appUsers
                  ? appUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="memory-tag" name="tag" data-cy="tag" label={translate('manageYourBrainApp.memory.tag')} type="select">
                <option value="" key="0" />
                {tags
                  ? tags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="memory-mnemonic"
                name="mnemonic"
                data-cy="mnemonic"
                label={translate('manageYourBrainApp.memory.mnemonic')}
                type="select"
              >
                <option value="" key="0" />
                {mnemonics
                  ? mnemonics.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/memory" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MemoryUpdate;
