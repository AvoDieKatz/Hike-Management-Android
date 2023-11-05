package com.example.mhikeandroidapp.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhikeandroidapp.R;
import com.example.mhikeandroidapp.adapter.HikeAdapter;
import com.example.mhikeandroidapp.adapter.ObservationAdapter;
import com.example.mhikeandroidapp.db.DatabaseHelper;
import com.example.mhikeandroidapp.db.entity.hike.Hike;
import com.example.mhikeandroidapp.db.entity.hike.HikeDTO;
import com.example.mhikeandroidapp.db.entity.observation.Observation;
import com.example.mhikeandroidapp.db.entity.observation.ObservationDTO;
import com.example.mhikeandroidapp.db.repository.hike.HikeRepositoryImpl;
import com.example.mhikeandroidapp.db.repository.observation.ObservationRepositoryImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HikeDetailFragment extends Fragment {
    private Hike selectedHike;
    private HikeRepositoryImpl hikeRepository;
    private LinearLayout detailLayout;
    private LinearLayout editLayout;
    private EditText titleInput;
    private EditText locationInput;
    private EditText dateInput;
    private EditText lengthInput;
    private RadioGroup difficultInput;
    private EditText descriptionInput;
    private CheckBox parkingInput;

    private ObservationAdapter observationAdapter;
    private List<Observation> observationList = new ArrayList<>();
    private ObservationRepositoryImpl observationRepository;

    private RecyclerView observationRecyclerView;
    private EditText observationInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedHike = bundle.getParcelable("hikeDetail", Hike.class);
        }
        return inflater.inflate(R.layout.hike_detail, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hikeRepository = new HikeRepositoryImpl(new DatabaseHelper(requireContext()));

        detailLayout = view.findViewById(R.id.hike_detail_layout);
        editLayout = view.findViewById(R.id.hike_edit_layout);

        final TextView hikeTitle = view.findViewById(R.id.hike_title);
        final TextView hikeDescription = view.findViewById(R.id.hike_description);
        final TextView hikeLocation = view.findViewById(R.id.hike_location);
        final TextView hikeDate = view.findViewById(R.id.hike_date);
        final TextView hikeLength = view.findViewById(R.id.hike_length);
        final TextView hikeDifficulty = view.findViewById(R.id.hike_difficulty);
        final TextView hikeParking = view.findViewById(R.id.hike_parking);

        String difficultyStr = switch (selectedHike.getDifficulty()) {
            case 108 -> "Easy";
            case 109 -> "Hard";
            case 110 -> "Medium";
            default -> "";
        };

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("E, d L, yyyy");

        hikeTitle.setText(getString(R.string.text, selectedHike.getTitle()));
        hikeDate.setText(selectedHike.getDate().format(formatters));
        hikeDescription.setText(getString(R.string.text, selectedHike.getDescription()));
        hikeLocation.setText(getString(R.string.text, selectedHike.getLocation()));
        hikeLength.setText(getString(R.string.hike_length, selectedHike.getLength()));
        hikeDifficulty.setText(difficultyStr);
        hikeParking.setText(selectedHike.isParking() ? "Available" : "Not Available");


        /**
         *
         * SET UP OBSERVATION RECYCLER VIEW
         *
         */
        observationRepository = new ObservationRepositoryImpl(new DatabaseHelper(requireContext()));

        observationList.addAll(observationRepository.getHikeObservations(selectedHike.getId()));
        observationAdapter = new ObservationAdapter(requireContext(), observationList, requireActivity(), HikeDetailFragment.this);

        observationRecyclerView = view.findViewById(R.id.recycler_view_observations);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        observationRecyclerView.setLayoutManager(layoutManager);
        observationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        observationRecyclerView.setAdapter(observationAdapter);


        /**
         *
         * SET UP EDIT TEXT
         *
         */
        titleInput = getView().findViewById(R.id.hike_title_input);
        locationInput = getView().findViewById(R.id.hike_location_input);
        dateInput = getView().findViewById(R.id.hike_date_input);
        lengthInput = getView().findViewById(R.id.hike_length_input);
        difficultInput = getView().findViewById(R.id.hike_difficulty_input);
        descriptionInput = getView().findViewById(R.id.hike_description_input);
        parkingInput = getView().findViewById(R.id.hike_parking_input);


        /**
         *
         * SET UP EDIT BUTTON
         *
         */
        final Button editBtn = view.findViewById(R.id.hike_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultData();
                setLayout("edit");
            }
        });

        /**
         *
         * SET UP BACK BUTTON
         *
         */

        final Button backBtn = view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReturn();
            }
        });

        /**
         *
         * SET UP UPDATE BUTTON
         *
         */

        final Button saveBtn = view.findViewById(R.id.edit_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performUpdateDetails();
//                setLayout("detail");
                handleReturn();
            }
        });

        /**
         *
         * SET UP CANCEL BUTTON
         *
         */

        final Button cancelBtn = view.findViewById(R.id.edit_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLayout("detail");
                setDefaultData();
            }
        });


    }

    private void handleReturn() {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        HikeListFragment listFragment = new HikeListFragment();

        ft.replace(R.id.fragment_content_view, listFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void setDefaultData() {
        /**
         * Setup Date Picker for Date input
         *
         */
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateInput.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                }, year, month, day
                );

                datePickerDialog.show();
            }
        });

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        titleInput.setText(this.selectedHike.getTitle());
        locationInput.setText(this.selectedHike.getLocation());
        dateInput.setText(this.selectedHike.getDate().format(df));
        lengthInput.setText(String.valueOf(this.selectedHike.getLength()));
        switch (this.selectedHike.getDifficulty()) {
            case 108: difficultInput.check(getView().findViewById(R.id.rad_easy_difficulty).getId());
            case 109: difficultInput.check(getView().findViewById(R.id.rad_hard_difficulty).getId());
            case 110: difficultInput.check(getView().findViewById(R.id.rad_medium_difficulty).getId());
            default:
        }
        descriptionInput.setText(this.selectedHike.getDescription());
        parkingInput.setChecked(this.selectedHike.isParking());
    }

    private void setLayout(String viewType) {
        if (viewType == "detail") {
            editLayout.setVisibility(View.GONE);
            detailLayout.setVisibility(View.VISIBLE);
        } else if (viewType == "edit") {
            detailLayout.setVisibility(View.GONE);
            editLayout.setVisibility(View.VISIBLE);
        }
    }

    private void performUpdateDetails() {
        Log.d("HIKE", "UPDATED");
        boolean isValid = checkFormValidity();
        if (isValid) {
            HikeDTO hikeDto = new HikeDTO();
            hikeDto.setTitle(titleInput.getText().toString());
            hikeDto.setLocation(locationInput.getText().toString());
            hikeDto.setDate(
                    LocalDate.parse(
                            dateInput.getText().toString(),
                            DateTimeFormatter.ofPattern("d-M-y").withLocale(Locale.getDefault())
                    )
            );
            hikeDto.setLength(Float.parseFloat(lengthInput.getText().toString()));
            hikeDto.setDifficulty((byte) difficultInput.getCheckedRadioButtonId());
            hikeDto.setDescription(descriptionInput.getText().toString());
            hikeDto.setParking(parkingInput.isChecked());

            int updatedHikeId = hikeRepository.updateHike(this.selectedHike.getId(), hikeDto);
            Hike updatedHike = hikeRepository.getSingleHike(updatedHikeId);
            if (updatedHike != null) {
                this.selectedHike.setTitle(updatedHike.getTitle());
                this.selectedHike.setLocation(updatedHike.getLocation());
                this.selectedHike.setDate(updatedHike.getDate());
                this.selectedHike.setLength(updatedHike.getLength());
                this.selectedHike.setDifficulty(updatedHike.getDifficulty());
                this.selectedHike.setDescription(updatedHike.getDescription());
                this.selectedHike.setParking(updatedHike.isParking());
            }
        }

    }

    private boolean checkFormValidity() {
        boolean valid = true;
        if (titleInput.length() == 0) {
            titleInput.setError("Field is required.");
            valid = false;
        }
        if (locationInput.length() == 0) {
            locationInput.setError("Field is required.");
            valid = false;
        }
        if (dateInput.length() == 0) {
            dateInput.setError("Field is required.");
            valid = false;
        }
        if (lengthInput.length() == 0) {
            lengthInput.setError("Field is required.");
            valid = false;
        }
        return valid;
    }

    public void openAddObservationDialog() {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.observation_add, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        observationInput = view.findViewById(R.id.observation_input);

        dialogBuilder
                .setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wantToClose = false;
                boolean formValid = observationInput.length() > 0;
                if (!formValid) {
                    observationInput.setError("Field is required!");
                } else {
                    ObservationDTO newObservation = new ObservationDTO();
                    newObservation.setObservation(observationInput.getText().toString());
                    newObservation.setHikeId(selectedHike.getId());
                    createObservation(newObservation);
                    wantToClose = true;
                }
                if (wantToClose) {
                    dialog.dismiss();
                    observationRecyclerView.scrollToPosition(0);
                }
            }
        });
    }

    private void createObservation(ObservationDTO observationDTO) {
        int createdObservationId = observationRepository.createObservation(observationDTO);
        Observation createdObservation =  observationRepository.getSingleObservation(createdObservationId);
        if (createdObservation != null) {
            observationList.add(0, createdObservation);
            observationAdapter.notifyItemInserted(0);
        }
    }

}
