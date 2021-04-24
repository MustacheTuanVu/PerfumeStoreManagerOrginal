import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './stores.reducer';
import { IStores } from 'app/shared/model/stores.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IStoresUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StoresUpdate = (props: IStoresUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { storesEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/stores' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...storesEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="perfumeStoreManagerApp.stores.home.createOrEditLabel" data-cy="StoresCreateUpdateHeading">
            <Translate contentKey="perfumeStoreManagerApp.stores.home.createOrEditLabel">Create or edit a Stores</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : storesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="stores-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="stores-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="storeIDLabel" for="stores-storeID">
                  <Translate contentKey="perfumeStoreManagerApp.stores.storeID">Store ID</Translate>
                </Label>
                <AvField id="stores-storeID" data-cy="storeID" type="text" name="storeID" />
              </AvGroup>
              <AvGroup>
                <Label id="storeNameLabel" for="stores-storeName">
                  <Translate contentKey="perfumeStoreManagerApp.stores.storeName">Store Name</Translate>
                </Label>
                <AvField id="stores-storeName" data-cy="storeName" type="text" name="storeName" />
              </AvGroup>
              <AvGroup>
                <Label id="storePhoneLabel" for="stores-storePhone">
                  <Translate contentKey="perfumeStoreManagerApp.stores.storePhone">Store Phone</Translate>
                </Label>
                <AvField id="stores-storePhone" data-cy="storePhone" type="text" name="storePhone" />
              </AvGroup>
              <AvGroup>
                <Label id="storeAdressLabel" for="stores-storeAdress">
                  <Translate contentKey="perfumeStoreManagerApp.stores.storeAdress">Store Adress</Translate>
                </Label>
                <AvField id="stores-storeAdress" data-cy="storeAdress" type="text" name="storeAdress" />
              </AvGroup>
              <AvGroup>
                <Label id="storeRentLabel" for="stores-storeRent">
                  <Translate contentKey="perfumeStoreManagerApp.stores.storeRent">Store Rent</Translate>
                </Label>
                <AvField id="stores-storeRent" data-cy="storeRent" type="string" className="form-control" name="storeRent" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/stores" replace color="info">
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
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  storesEntity: storeState.stores.entity,
  loading: storeState.stores.loading,
  updating: storeState.stores.updating,
  updateSuccess: storeState.stores.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StoresUpdate);
