import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICustomers } from 'app/shared/model/customers.model';
import { getEntities as getCustomers } from 'app/entities/customers/customers.reducer';
import { IMember } from 'app/shared/model/member.model';
import { getEntities as getMembers } from 'app/entities/member/member.reducer';
import { getEntity, updateEntity, createEntity, reset } from './bills.reducer';
import { IBills } from 'app/shared/model/bills.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBillsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const BillsUpdate = (props: IBillsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { billsEntity, customers, members, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/bills' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCustomers();
    props.getMembers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...billsEntity,
        ...values,
        customers: customers.find(it => it.id.toString() === values.customersId.toString()),
        member: members.find(it => it.id.toString() === values.memberId.toString()),
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
          <h2 id="perfumeStoreManagerApp.bills.home.createOrEditLabel" data-cy="BillsCreateUpdateHeading">
            <Translate contentKey="perfumeStoreManagerApp.bills.home.createOrEditLabel">Create or edit a Bills</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : billsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="bills-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="bills-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="billIDLabel" for="bills-billID">
                  <Translate contentKey="perfumeStoreManagerApp.bills.billID">Bill ID</Translate>
                </Label>
                <AvField id="bills-billID" data-cy="billID" type="text" name="billID" />
              </AvGroup>
              <AvGroup>
                <Label id="salesIDLabel" for="bills-salesID">
                  <Translate contentKey="perfumeStoreManagerApp.bills.salesID">Sales ID</Translate>
                </Label>
                <AvField id="bills-salesID" data-cy="salesID" type="text" name="salesID" />
              </AvGroup>
              <AvGroup>
                <Label id="dateLabel" for="bills-date">
                  <Translate contentKey="perfumeStoreManagerApp.bills.date">Date</Translate>
                </Label>
                <AvField id="bills-date" data-cy="date" type="date" className="form-control" name="date" />
              </AvGroup>
              <AvGroup>
                <Label id="discountLabel" for="bills-discount">
                  <Translate contentKey="perfumeStoreManagerApp.bills.discount">Discount</Translate>
                </Label>
                <AvField id="bills-discount" data-cy="discount" type="string" className="form-control" name="discount" />
              </AvGroup>
              <AvGroup>
                <Label id="vatLabel" for="bills-vat">
                  <Translate contentKey="perfumeStoreManagerApp.bills.vat">Vat</Translate>
                </Label>
                <AvField id="bills-vat" data-cy="vat" type="string" className="form-control" name="vat" />
              </AvGroup>
              <AvGroup>
                <Label id="paymentLabel" for="bills-payment">
                  <Translate contentKey="perfumeStoreManagerApp.bills.payment">Payment</Translate>
                </Label>
                <AvField id="bills-payment" data-cy="payment" type="string" className="form-control" name="payment" />
              </AvGroup>
              <AvGroup>
                <Label id="totalLabel" for="bills-total">
                  <Translate contentKey="perfumeStoreManagerApp.bills.total">Total</Translate>
                </Label>
                <AvField id="bills-total" data-cy="total" type="string" className="form-control" name="total" />
              </AvGroup>
              <AvGroup>
                <Label id="customerIDLabel" for="bills-customerID">
                  <Translate contentKey="perfumeStoreManagerApp.bills.customerID">Customer ID</Translate>
                </Label>
                <AvField id="bills-customerID" data-cy="customerID" type="text" name="customerID" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="bills-status">
                  <Translate contentKey="perfumeStoreManagerApp.bills.status">Status</Translate>
                </Label>
                <AvField id="bills-status" data-cy="status" type="string" className="form-control" name="status" />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="bills-description">
                  <Translate contentKey="perfumeStoreManagerApp.bills.description">Description</Translate>
                </Label>
                <AvField id="bills-description" data-cy="description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label for="bills-customers">
                  <Translate contentKey="perfumeStoreManagerApp.bills.customers">Customers</Translate>
                </Label>
                <AvInput id="bills-customers" data-cy="customers" type="select" className="form-control" name="customersId">
                  <option value="" key="0" />
                  {customers
                    ? customers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="bills-member">
                  <Translate contentKey="perfumeStoreManagerApp.bills.member">Member</Translate>
                </Label>
                <AvInput id="bills-member" data-cy="member" type="select" className="form-control" name="memberId">
                  <option value="" key="0" />
                  {members
                    ? members.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/bills" replace color="info">
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
  customers: storeState.customers.entities,
  members: storeState.member.entities,
  billsEntity: storeState.bills.entity,
  loading: storeState.bills.loading,
  updating: storeState.bills.updating,
  updateSuccess: storeState.bills.updateSuccess,
});

const mapDispatchToProps = {
  getCustomers,
  getMembers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(BillsUpdate);
