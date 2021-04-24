import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './customers.reducer';
import { ICustomers } from 'app/shared/model/customers.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomersUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomersUpdate = (props: ICustomersUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { customersEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/customers' + props.location.search);
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
        ...customersEntity,
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
          <h2 id="perfumeStoreManagerApp.customers.home.createOrEditLabel" data-cy="CustomersCreateUpdateHeading">
            <Translate contentKey="perfumeStoreManagerApp.customers.home.createOrEditLabel">Create or edit a Customers</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : customersEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="customers-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="customers-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="customerIDLabel" for="customers-customerID">
                  <Translate contentKey="perfumeStoreManagerApp.customers.customerID">Customer ID</Translate>
                </Label>
                <AvField id="customers-customerID" data-cy="customerID" type="text" name="customerID" />
              </AvGroup>
              <AvGroup>
                <Label id="customerNameLabel" for="customers-customerName">
                  <Translate contentKey="perfumeStoreManagerApp.customers.customerName">Customer Name</Translate>
                </Label>
                <AvField id="customers-customerName" data-cy="customerName" type="text" name="customerName" />
              </AvGroup>
              <AvGroup>
                <Label id="addressLabel" for="customers-address">
                  <Translate contentKey="perfumeStoreManagerApp.customers.address">Address</Translate>
                </Label>
                <AvField id="customers-address" data-cy="address" type="text" name="address" />
              </AvGroup>
              <AvGroup>
                <Label id="phoneLabel" for="customers-phone">
                  <Translate contentKey="perfumeStoreManagerApp.customers.phone">Phone</Translate>
                </Label>
                <AvField id="customers-phone" data-cy="phone" type="text" name="phone" />
              </AvGroup>
              <AvGroup>
                <Label id="emailLabel" for="customers-email">
                  <Translate contentKey="perfumeStoreManagerApp.customers.email">Email</Translate>
                </Label>
                <AvField id="customers-email" data-cy="email" type="text" name="email" />
              </AvGroup>
              <AvGroup>
                <Label id="genderLabel" for="customers-gender">
                  <Translate contentKey="perfumeStoreManagerApp.customers.gender">Gender</Translate>
                </Label>
                <AvField id="customers-gender" data-cy="gender" type="string" className="form-control" name="gender" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/customers" replace color="info">
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
  customersEntity: storeState.customers.entity,
  loading: storeState.customers.loading,
  updating: storeState.customers.updating,
  updateSuccess: storeState.customers.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomersUpdate);
