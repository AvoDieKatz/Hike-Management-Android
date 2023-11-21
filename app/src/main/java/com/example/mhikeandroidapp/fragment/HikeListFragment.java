package com.example.mhikeandroidapp.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhikeandroidapp.R;
import com.example.mhikeandroidapp.adapter.HikeAdapter;
import com.example.mhikeandroidapp.callback.SwipeToDeleteCallback;
import com.example.mhikeandroidapp.db.DatabaseHelper;
import com.example.mhikeandroidapp.db.entity.hike.Hike;
import com.example.mhikeandroidapp.db.entity.hike.HikeDTO;
import com.example.mhikeandroidapp.db.repository.hike.HikeRepositoryImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HikeListFragment extends Fragment {
    private HikeAdapter hikeAdapter;
    private List<Hike> hikeList = new ArrayList<>();
    private List<Hike> originalList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HikeRepositoryImpl hikeRepository;
    private SearchView searchView;
    boolean isFormValid = false;
    EditText titleInput;
    EditText locationInput;
    EditText dateInput;
    EditText lengthInput;
    RadioGroup difficultInput;
    EditText descriptionInput;
    CheckBox parkingInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LIST FRAGMENT", "onCreateView() executed!");
        return inflater.inflate(R.layout.hike_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hikeRepository = new HikeRepositoryImpl(new DatabaseHelper(requireContext()));

        hikeList.addAll(hikeRepository.getHikeList());
        originalList.addAll(hikeList);
        hikeAdapter = new HikeAdapter(requireContext(), hikeList, requireActivity());

        recyclerView = view.findViewById(R.id.recycler_view_hikes);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hikeAdapter);

        searchView = view.findViewById(R.id.searchBar);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this.getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int itemPosition = viewHolder.getAdapterPosition();
                Hike swipedHike = hikeList.get(itemPosition);
                removeHikeFromList(itemPosition);
                hikeAdapter.notifyItemRemoved(itemPosition);

                Handler handler = new Handler();
                Runnable deleteRunnable = () -> deleteHike(swipedHike);

                // If Snackbar's Action is not clicked, proceed to delete
                handler.postDelayed(deleteRunnable, Snackbar.LENGTH_LONG + 5000);

                Snackbar undoSnackBar = Snackbar.make(view, "Hike removed!", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v -> {
                            Log.d("SNACK", "ACTION CLICKED!");

                            //remove delay
                            handler.removeCallbacks(deleteRunnable);

                            restoreHikeToList(itemPosition, swipedHike);
                            hikeAdapter.notifyItemInserted(itemPosition);
                        });
                undoSnackBar.show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton addHikeBtn = getView().findViewById(R.id.btn_add_hike);
        addHikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHike();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("LIST FRAGMENT", "onDestroyView() executed!");
    }

    private void filterList(String text) {
        List<Hike> filteredList = new ArrayList<>();
        if (text.equals("")) {
            filteredList.addAll(hikeRepository.getHikeList());
        } else {
            for (Hike hike : originalList) {
                if (hike.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(hike);
                }
            }
        }
        int prevSize = hikeList.size();
        hikeList.clear();
        hikeAdapter.notifyItemRangeRemoved(0, prevSize);
        hikeList.addAll(filteredList);
        hikeAdapter.notifyItemRangeInserted(0, hikeList.size());
    }

    private void addHike() {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.hike_add, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        titleInput = view.findViewById(R.id.hike_title);
        locationInput = view.findViewById(R.id.hike_location);
        dateInput = view.findViewById(R.id.hike_date);
        lengthInput = view.findViewById(R.id.hike_length);
        difficultInput = view.findViewById(R.id.hike_difficulty);
        descriptionInput = view.findViewById(R.id.hike_description);
        parkingInput = view.findViewById(R.id.hike_parking);

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

        // Set default for radio button difficulty
        difficultInput.check(view.findViewById(R.id.rad_medium_difficulty).getId());

        dialogBuilder
                .setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Override later after create()
                        // By doing this, we can stop dialog from closing once clicked, used to perform validation
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                isFormValid = checkValidity();

                if (isFormValid) {
                    HikeDTO newHike = new HikeDTO();
                    newHike.setTitle(titleInput.getText().toString());
                    newHike.setLocation(locationInput.getText().toString());
                    newHike.setDate(
                            LocalDate.parse(
                                    dateInput.getText().toString(),
                                    DateTimeFormatter.ofPattern("d-M-y").withLocale(Locale.getDefault())
                            )
                    );
                    newHike.setLength(Float.parseFloat(lengthInput.getText().toString()));
                    newHike.setDifficulty((byte) difficultInput.getCheckedRadioButtonId());
                    newHike.setDescription(descriptionInput.getText().toString());
                    newHike.setParking(parkingInput.isChecked());

                    createHike(newHike);
                    Snackbar.make(view, "New hike added!", Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
    }


    private boolean checkValidity() {
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

    public void createHike(HikeDTO hikeDTO) {
        int createdHikeId = hikeRepository.createHike(hikeDTO);
        Hike createdHike = hikeRepository.getSingleHike(createdHikeId);
        if (createdHike != null) {
            hikeList.add(0, createdHike);
            hikeAdapter.notifyItemInserted(0);
        }
    }

    private void removeHikeFromList(int position) {
        hikeList.remove(position);
    }

    private void restoreHikeToList(int position, Hike hike) {
        hikeList.add(position, hike);
    }

    public void deleteHike(Hike hike) {
        int hikeId = hike.getId();
        hikeRepository.deleteHike(hikeId);
    }

}
